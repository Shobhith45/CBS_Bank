package com.cbsindia.cbs.data.repository.base

import com.cbsindia.cbs.data.models.Login
import retrofit2.Response

interface LoginRepository {
    suspend fun login(
        customerID: String,
        mPin: String
    ): Response<Login>
}
