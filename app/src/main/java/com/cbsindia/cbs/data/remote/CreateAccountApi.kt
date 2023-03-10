package com.cbsindia.cbs.data.remote

import com.cbsindia.cbs.data.models.CreateAccount
import com.cbsindia.cbs.util.Constant.ENDPOINT_CREATE_ACCOUNT
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface CreateAccountApi {
    @POST(ENDPOINT_CREATE_ACCOUNT)
    suspend fun createAccount(
        @Body body: HashMap<String, String>
    ): Response<CreateAccount>
}
