package com.cbsindia.cbs.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cbsindia.cbs.data.models.Login
import com.cbsindia.cbs.data.remote.ResponseState
import com.cbsindia.cbs.data.repository.base.LoginRepository
import com.cbsindia.cbs.data.repository.base.PrefRepository
import com.cbsindia.cbs.util.Constant.KEY_BEARER
import com.cbsindia.cbs.util.Constant.MPIN_LENGTH
import com.cbsindia.cbs.util.Event
import com.google.gson.Gson
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.lang.Exception

class LoginViewModel(
    private val loginRepository: LoginRepository
) : ViewModel(), KoinComponent {

    private val prefRepository: PrefRepository by inject()
    private val gson: Gson by inject()

    private val _formLiveData: MutableLiveData<Event<LoginEvent>> = MutableLiveData()
    val formLiveData: LiveData<Event<LoginEvent>> = _formLiveData

    private val _loginResponse: MutableLiveData<Event<ResponseState<Login>>> = MutableLiveData()
    val loginViewModel: LiveData<Event<ResponseState<Login>>> = _loginResponse

    fun validateForm(customerId: String, mpin: String, savedMPin: String,isUserLoggedIn: Boolean) {
        _formLiveData.value =
            when {
                customerId.isEmpty() -> Event(LoginEvent.EmptyCustomerId)
                mpin.isEmpty() -> Event(LoginEvent.EmptyMpin)
                mpin.length != MPIN_LENGTH -> Event(LoginEvent.LessMpinLength)
                isUserLoggedIn && (savedMPin != mpin) -> Event(LoginEvent.ValidMPin)
                else -> Event(LoginEvent.Success)
            }
    }

    fun login(customerId: String, mpin: String) {
        viewModelScope.launch {
            _loginResponse.postValue(Event(ResponseState.loading()))
            try {
                val response = loginRepository.login(customerId, mpin)
                if (response.isSuccessful) {
                    _loginResponse.postValue(Event(ResponseState.success(response.body())))
                } else {
                    val error = gson.fromJson(
                        response.errorBody()?.charStream(),
                        Login::class.java
                    )
                    _loginResponse.postValue(Event(ResponseState.error(error.resultInfo.message)))
                }
            } catch (e: Exception) {
                _loginResponse.postValue(Event(ResponseState.error(e.message.toString())))
            }
        }
    }

    fun saveLoginInfo(token: String, customerId: String, isLoggedIn: Boolean, mPin: Long) {
        prefRepository.setCustomerId(customerId)
        prefRepository.setUserToken("$KEY_BEARER $token")
        prefRepository.setIsUserLoggedIn(isLoggedIn)
        prefRepository.setUserMpin(mPin)
    }

    fun checkIfAlreadyLoggedIn(): Boolean {
        return prefRepository.getIsUserLoggedIn()
    }

    fun getCustomerId(): String {
        return prefRepository.getCustomerId().toString()
    }

    fun resetToken() {
        prefRepository.setUserToken("")
    }
}

sealed class LoginEvent {
    object EmptyCustomerId : LoginEvent()
    object EmptyMpin : LoginEvent()
    object LessMpinLength : LoginEvent()
    object ValidMPin : LoginEvent()
    object Success : LoginEvent()
}
