package com.cbsindia.cbs.data.remote

import com.cbsindia.cbs.data.models.Deposit
import com.cbsindia.cbs.util.Constant.END_POINT_DEPOSIT
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface DepositApi {
    @POST(END_POINT_DEPOSIT)
    suspend fun deposit(
        @Body body: Double
    ): Response<Deposit>
}
