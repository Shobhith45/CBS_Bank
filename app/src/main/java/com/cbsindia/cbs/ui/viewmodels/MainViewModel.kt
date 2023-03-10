package com.cbsindia.cbs.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cbsindia.cbs.data.models.Balance
import com.cbsindia.cbs.data.models.UserAccount
import com.cbsindia.cbs.data.remote.ResponseState
import com.cbsindia.cbs.data.repository.base.BalanceRepository
import com.cbsindia.cbs.data.repository.base.PrefRepository
import com.cbsindia.cbs.data.repository.base.UserAccountRepository
import com.cbsindia.cbs.util.Constant
import com.cbsindia.cbs.util.Constant.UNAUTHORIZED_ERROR_CODE
import com.cbsindia.cbs.util.SessionManager
import com.google.gson.Gson
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import retrofit2.Response
import kotlin.random.Random

class MainViewModel(
    private val balanceRepository: BalanceRepository,
    private val userAccountRepository: UserAccountRepository
) : ViewModel(), KoinComponent {

    private val gson: Gson by inject()
    private val prefRepository: PrefRepository by inject()
    private val userPosition = Random.nextInt(2)
    private val _balanceLiveData: MutableLiveData<ResponseState<Balance>> = MutableLiveData()
    val balanceLiveData: LiveData<ResponseState<Balance>> = _balanceLiveData
    private val _userLiveData: MutableLiveData<ResponseState<UserAccount>> = MutableLiveData()
    val userLiveData: LiveData<ResponseState<UserAccount>> = _userLiveData
    var balance: Double = 0.0

    fun getBalance() {
        viewModelScope.launch {
            _balanceLiveData.postValue(ResponseState.loading())
            try {
                val response = balanceRepository.getBalance()
                if (response.isSuccessful) {
                    _balanceLiveData.postValue(ResponseState.success(response.body()))
                    balance = response.body()!!.balance
                } else {
                    handleGetBalanceResponseError(response)
                }
            } catch (e: Exception) {
                _balanceLiveData.postValue(ResponseState.error(e.message.toString()))
            }
        }
    }

    private fun handleGetBalanceResponseError(response: Response<Balance>) {
        if (response.code() == UNAUTHORIZED_ERROR_CODE) {
            SessionManager.sessionExpired()
            return
        }
        val error: Balance? =
            gson.fromJson(
                response.errorBody()?.charStream(),
                Balance::class.java
            )
        _balanceLiveData.postValue(
            ResponseState.error(
                error?.resultInfo?.message ?: Constant.API_ERROR
            )
        )
    }

    fun getUserAccount() {
        viewModelScope.launch {
            val response = userAccountRepository.getUserAccount(userPosition)
            _userLiveData.postValue(ResponseState.success(response))
        }
    }

    fun logoutUser() {
        prefRepository.setIsUserLoggedIn(false)
        prefRepository.setUserToken("")
    }
}
