package com.cbsindia.cbs.data.repository.impl

import com.cbsindia.cbs.data.models.Login
import com.cbsindia.cbs.data.remote.LoginApi
import com.cbsindia.cbs.data.repository.base.LoginRepository
import retrofit2.Response

class LoginRepositoryImpl(
    private val loginApi: LoginApi
) : LoginRepository {
    override suspend fun login(
        customerID: String,
        mPin: String
    ): Response<Login> =
        loginApi.login(customerID, mPin)
}
