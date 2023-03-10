package com.cbsindia.cbs.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cbsindia.cbs.data.models.Deposit
import com.cbsindia.cbs.data.remote.ResponseState
import com.cbsindia.cbs.data.repository.base.DepositRepository
import com.cbsindia.cbs.util.Constant
import com.cbsindia.cbs.util.Constant.API_ERROR
import com.cbsindia.cbs.util.Constant.MIN_AMOUNT
import com.cbsindia.cbs.util.Constant.UNAUTHORIZED_ERROR_CODE
import com.cbsindia.cbs.util.Event
import com.cbsindia.cbs.util.SessionManager
import com.google.gson.Gson
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import retrofit2.Response

class DepositViewModel(
    private val depositRepository: DepositRepository
) : ViewModel(), KoinComponent {

    private val gson: Gson by inject()

    private val _formResponse: MutableLiveData<Event<DepositEvent>> = MutableLiveData()
    val formResponse: LiveData<Event<DepositEvent>> = _formResponse

    private val _depositResponse: MutableLiveData<Event<ResponseState<Deposit>>> = MutableLiveData()
    val depositResponse: LiveData<Event<ResponseState<Deposit>>> = _depositResponse

    fun validateForm(amount: String, mPin: String, savedMPin: Long) {
        when {
            amount.isEmpty() -> _formResponse.value = Event(DepositEvent.EmptyBalance)
            amount.toDouble() <= MIN_AMOUNT -> _formResponse.value = Event(DepositEvent.ZeroAmount)
            mPin.isEmpty() -> _formResponse.value = Event(DepositEvent.EmptyMpin)
            amount.toDouble() > Constant.MAX_AMOUNT ->
                _formResponse.value =
                    Event(DepositEvent.LargeAmount)
            mPin.length != Constant.MPIN_LENGTH ->
                _formResponse.value =
                    Event(DepositEvent.LessMpinLength)
            mPin.toLong() != savedMPin -> _formResponse.value = Event(DepositEvent.MpinNotMatching)
            else -> deposit(amount.toDouble())
        }
    }

    private fun deposit(body: Double) {
        viewModelScope.launch {
            _depositResponse.postValue(Event(ResponseState.loading()))
            try {
                val response = depositRepository.deposit(body)
                if (response.isSuccessful) {
                    _depositResponse.postValue(Event(ResponseState.success(response.body())))
                } else {
                    handleDepositResponseError(response)
                }
            } catch (e: Exception) {
                _depositResponse.postValue(
                    Event(
                        ResponseState.error(
                            e.message.toString()
                        )
                    )
                )
            }
        }
    }

    private fun handleDepositResponseError(response: Response<Deposit>) {
        if (response.code() == UNAUTHORIZED_ERROR_CODE) {
            SessionManager.sessionExpired()
            return
        }
        val error: Deposit? = gson.fromJson(
            response.errorBody()?.charStream(),
            Deposit::class.java
        )
        _depositResponse.postValue(
            Event(
                ResponseState.error(
                    error?.resultInfo?.message ?: API_ERROR
                )
            )
        )
    }
}

sealed class DepositEvent {
    object EmptyBalance : DepositEvent()
    object EmptyMpin : DepositEvent()
    object LessMpinLength : DepositEvent()
    object MpinNotMatching : DepositEvent()
    object LargeAmount : DepositEvent()
    object ZeroAmount : DepositEvent()
}
