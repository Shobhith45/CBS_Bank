package com.cbsindia.cbs.data.remote

import com.cbsindia.cbs.data.models.Balance
import com.cbsindia.cbs.util.Constant.ENDPOINT_BALANCE
import retrofit2.Response
import retrofit2.http.GET

interface BalanceApi {
    @GET(ENDPOINT_BALANCE)
    suspend fun getBalance(): Response<Balance>
}
