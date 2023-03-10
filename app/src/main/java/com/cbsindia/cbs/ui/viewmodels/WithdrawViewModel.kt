package com.cbsindia.cbs.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cbsindia.cbs.data.models.Withdraw
import com.cbsindia.cbs.data.remote.ResponseState
import com.cbsindia.cbs.data.repository.base.WithdrawRepository
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
import java.util.concurrent.TimeoutException

class WithdrawViewModel(
    private val withdrawRepository: WithdrawRepository
) : ViewModel(), KoinComponent {

    private val gson: Gson by inject()

    private val _formResponse: MutableLiveData<Event<WithdrawEvent>> = MutableLiveData()
    val formResponse: LiveData<Event<WithdrawEvent>> = _formResponse

    private val _withdrawResponse: MutableLiveData<Event<ResponseState<Withdraw>>> =
        MutableLiveData()
    val withdrawAmtViewModel: LiveData<Event<ResponseState<Withdraw>>> = _withdrawResponse

    fun validateForm(amount: String, mPin: String, savedMPin: Long, balance: Double) {
        _formResponse.value =
            when {
                amount.isEmpty() -> Event(WithdrawEvent.EmptyBalance)
                amount.toDouble() <= MIN_AMOUNT -> Event(WithdrawEvent.ZeroAmount)
                mPin.isEmpty() -> Event(WithdrawEvent.EmptyMpin)
                amount.toDouble() > MAX_AMOUNT -> Event(WithdrawEvent.LargeAmount)
                mPin.length != MPIN_LENGTH -> Event(WithdrawEvent.LessMpinLength)
                mPin.toLong() != savedMPin -> Event(WithdrawEvent.MpinNotMatching)
                amount.toDouble() > balance -> Event(WithdrawEvent.LowBalance)
                else -> Event(WithdrawEvent.Success)
            }
    }

    fun withdraw(body: Double) {
        viewModelScope.launch {
            _withdrawResponse.postValue(Event(ResponseState.loading()))
            try {
                val response = withdrawRepository.withdraw(body)
                if (response.isSuccessful) {
                    _withdrawResponse.postValue(Event(ResponseState.success(response.body())))
                } else {
                    handleWithdrawResponseError(response)
                }
            } catch (e: TimeoutException) {
            } catch (e: Exception) {
                _withdrawResponse.postValue(Event(ResponseState.error(e.message.toString())))
            }
        }
    }

    private fun handleWithdrawResponseError(response: Response<Withdraw>) {
        if (response.code() == UNAUTHORIZED_ERROR_CODE) {
            SessionManager.sessionExpired()
            return
        }
        val error: Withdraw? = gson.fromJson(
            response.errorBody()?.charStream(),
            Withdraw::class.java
        )
        _withdrawResponse.postValue(
            Event(ResponseState.error(error?.resultInfo?.message ?: API_ERROR))
        )
    }
}

sealed class WithdrawEvent {
    object EmptyBalance : WithdrawEvent()
    object EmptyMpin : WithdrawEvent()
    object LessMpinLength : WithdrawEvent()
    object LowBalance : WithdrawEvent()
    object MpinNotMatching : WithdrawEvent()
    object LargeAmount : WithdrawEvent()
    object Success : WithdrawEvent()
    object ZeroAmount : WithdrawEvent()
}
