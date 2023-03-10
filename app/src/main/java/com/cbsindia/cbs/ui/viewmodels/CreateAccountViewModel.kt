package com.cbsindia.cbs.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cbsindia.cbs.data.models.CreateAccount
import com.cbsindia.cbs.data.remote.ResponseState
import com.cbsindia.cbs.data.repository.base.CreateAccountRepository
import com.cbsindia.cbs.util.Event
import com.google.gson.Gson
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.lang.Exception

class CreateAccountViewModel(
    private val createAccountRepository: CreateAccountRepository
) : ViewModel(), KoinComponent {
    private val _formLiveData: MutableLiveData<Event<CreateAccountEvent>> = MutableLiveData()
    val formLiveData: LiveData<Event<CreateAccountEvent>> = _formLiveData

    private val gson: Gson by inject()

    private val _createAccountResponse: MutableLiveData<Event<ResponseState<CreateAccount>>> = MutableLiveData()
    val createAccountViewModel: LiveData<Event<ResponseState<CreateAccount>>> = _createAccountResponse

    fun validateForm(fullName: String, phoneNumber: String, UIDNumber: String, birthDate: String, parentsName: String, nomineeName: String, mpin: String, confirmMpin: String, address: String) {
        _formLiveData.value = when {
            fullName.isEmpty() -> Event(CreateAccountEvent.EmptyFullName)
            phoneNumber.isEmpty() -> Event(CreateAccountEvent.EmptyPhoneNumber)
            phoneNumber.length != 10 -> Event(CreateAccountEvent.LimitMobileNumber)
            UIDNumber.isEmpty() -> Event(CreateAccountEvent.EmptyUIDNumber)
            UIDNumber.length != 12 -> Event(CreateAccountEvent.LimitAadhaarNumber)
            birthDate.isEmpty() -> Event(CreateAccountEvent.EmptyBirthDate)
            address.isEmpty() -> Event(CreateAccountEvent.EmptyAddress)
            parentsName.isEmpty() -> Event(CreateAccountEvent.EmptyParentName)
            nomineeName.isEmpty() -> Event(CreateAccountEvent.EmptyNomineeName)
            mpin.isEmpty() -> Event(CreateAccountEvent.EmptyMPIN)
            mpin.length != 6 -> Event(CreateAccountEvent.LimitMPIN)
            confirmMpin.isEmpty() -> Event(CreateAccountEvent.EmptyConfirmMPIN)
            mpin != confirmMpin -> Event(CreateAccountEvent.MpinNotEqualsConfirmMpin)
            else -> Event(CreateAccountEvent.Success)
        }
    }
    fun createAccount(body: HashMap<String, String>) {
        viewModelScope.launch {
            _createAccountResponse.postValue(Event(ResponseState.loading()))
            try {
                val response = createAccountRepository.createAccount(body)
                if (response.isSuccessful) {
                    _createAccountResponse.postValue(Event(ResponseState.success(response.body())))
                } else {
                    val error = gson.fromJson(
                        response.errorBody()?.charStream(),
                        CreateAccount::class.java
                    )
                    _createAccountResponse.postValue(Event(ResponseState.error(error.resultInfo.message)))
                }
            } catch (e: Exception) {
                _createAccountResponse.postValue(Event(ResponseState.error(e.message.toString())))
            }
        }
    }
}
sealed class CreateAccountEvent {
    object EmptyFullName : CreateAccountEvent()
    object EmptyPhoneNumber : CreateAccountEvent()
    object EmptyUIDNumber : CreateAccountEvent()
    object EmptyBirthDate : CreateAccountEvent()
    object EmptyAddress : CreateAccountEvent()
    object EmptyParentName : CreateAccountEvent()
    object EmptyNomineeName : CreateAccountEvent()
    object EmptyMPIN : CreateAccountEvent()
    object EmptyConfirmMPIN : CreateAccountEvent()
    object LimitMobileNumber : CreateAccountEvent()
    object LimitAadhaarNumber : CreateAccountEvent()
    object LimitMPIN : CreateAccountEvent()
    object MpinNotEqualsConfirmMpin : CreateAccountEvent()
    object Success : CreateAccountEvent()
}
