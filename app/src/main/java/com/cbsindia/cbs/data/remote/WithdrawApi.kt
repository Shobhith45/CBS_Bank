package com.cbsindia.cbs.data.remote

import com.cbsindia.cbs.data.models.Withdraw
import com.cbsindia.cbs.util.Constant.END_POINT_WITHDRAW
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface WithdrawApi {
    @POST(END_POINT_WITHDRAW)
    suspend fun withdraw(
        @Body body: Double
    ): Response<Withdraw>
}
