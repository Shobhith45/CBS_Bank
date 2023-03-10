package com.cbsindia.cbs.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cbsindia.cbs.data.models.MoneyTransfer
import com.cbsindia.cbs.data.remote.ResponseState
import com.cbsindia.cbs.data.repository.base.MoneyTransferRepository
import com.cbsindia.cbs.util.Constant.API_ERROR
import com.cbsindia.cbs.util.Constant.MAX_AMOUNT
import com.cbsindia.cbs.util.Constant.MIN_AMOUNT
import com.cbsindia.cbs.util.Constant.MPIN_LENGTH
import com.cbsindia.cbs.util.Constant.UNAUTHORIZED_ERROR_CODE
import com.cbsindia.cbs.util.Event
import com.cbsindia.cbs.util.SessionManager
import com.google.gson.Gson
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import retrofit2.Response
import java.lang.Exception

class MoneyTransferViewModel(
    private val moneyTransferRepository: MoneyTransferRepository
) : ViewModel(), KoinComponent {

    private val gson: Gson by inject()
    private val _formLiveData: MutableLiveData<Event<MoneyTransferEvent>> = MutableLiveData()
    val formLiveData: LiveData<Event<MoneyTransferEvent>> = _formLiveData
    private val _transferResponse: MutableLiveData<Event<ResponseState<MoneyTransfer>>> =
        MutableLiveData()
    val transferViewModel: LiveData<Event<ResponseState<MoneyTransfer>>> = _transferResponse

    fun validateForm(
        amount: String,
        accountNumber: String,
        mPin: String,
        savedMpin: Long,
        balance: Double
    ) {
        _formLiveData.value =
            when {
                amount.isEmpty() -> Event(MoneyTransferEvent.EmptyAmount)
                amount.toDouble() <= MIN_AMOUNT -> Event(MoneyTransferEvent.ZeroAmount)
                accountNumber.isEmpty() -> Event(MoneyTransferEvent.EmptyAccountNumber)
                mPin.isEmpty() -> Event(MoneyTransferEvent.EmptyMpin)
                mPin.length != MPIN_LENGTH -> Event(MoneyTransferEvent.LessMpinLength)
                mPin.toLong() != savedMpin -> Event(MoneyTransferEvent.MpinNotMatching)
                amount.toDouble() > MAX_AMOUNT -> Event(MoneyTransferEvent.LargeAmount)
                amount.toDouble() > balance -> Event(MoneyTransferEvent.LowBalance)
                else -> Event(MoneyTransferEvent.Success)
            }
    }

    fun transferMoney(body: HashMap<String, Any>) {
        viewModelScope.launch {
            _transferResponse.postValue(Event(ResponseState.loading()))
            try {
                val response = moneyTransferRepository.transferMoney(body)
                if (response.isSuccessful) {
                    _transferResponse.postValue(
                        Event(
                            ResponseState.success(response.body())
                        )
                    )
                } else {
                    handleTransferMoneyResponseError(response)
                }
            } catch (e: Exception) {
                _transferResponse.postValue(
                    Event(
                        ResponseState.error(e.message.toString())
                    )
                )
            }
        }
    }

    private fun handleTransferMoneyResponseError(response: Response<MoneyTransfer>) {
        if (response.code() == UNAUTHORIZED_ERROR_CODE) {
            SessionManager.sessionExpired()
            return
        }
        val error: MoneyTransfer? = gson.fromJson(
            response.errorBody()?.charStream(),
            MoneyTransfer::class.java
        )
        _transferResponse.postValue(
            Event(
                ResponseState.error(
                    error?.resultInfo?.message ?: API_ERROR
                )
            )
        )
    }
}

sealed class MoneyTransferEvent {
    object EmptyAmount : MoneyTransferEvent()
    object ZeroAmount : MoneyTransferEvent()
    object EmptyAccountNumber : MoneyTransferEvent()
    object EmptyMpin : MoneyTransferEvent()
    object LessMpinLength : MoneyTransferEvent()
    object MpinNotMatching : MoneyTransferEvent()
    object LargeAmount : MoneyTransferEvent()
    object LowBalance : MoneyTransferEvent()
    object Success : MoneyTransferEvent()
}
