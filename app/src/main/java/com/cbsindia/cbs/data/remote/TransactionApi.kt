package com.cbsindia.cbs.data.remote

import com.cbsindia.cbs.data.models.TransactionResponse
import com.cbsindia.cbs.util.Constant.END_POINT_TRANSACTION
import com.cbsindia.cbs.util.Constant.KEY_PAGE_NUMBER
import com.cbsindia.cbs.util.Constant.KEY_PAGE_SIZE
import retrofit2.http.GET
import retrofit2.http.Query

interface TransactionApi {
    @GET(END_POINT_TRANSACTION)
    suspend fun getTransactions(
        @Query(KEY_PAGE_SIZE) pageSize: Int,
        @Query(KEY_PAGE_NUMBER) pageNumber: Int
    ): TransactionResponse
}
