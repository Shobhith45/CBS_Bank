package com.cbsindia.cbs.data.remote

import com.cbsindia.cbs.data.models.MoneyTransfer
import com.cbsindia.cbs.util.Constant.END_POINT_MONEY_TRANSFER
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface MoneyTransferApi {
    @POST(END_POINT_MONEY_TRANSFER)
    suspend fun transferMoney(
        @Body body: HashMap<String, Any>
    ): Response<MoneyTransfer>
}
