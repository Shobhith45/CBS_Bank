package com.cbsindia.cbs.data.repository.base

import com.cbsindia.cbs.data.models.Withdraw
import retrofit2.Response

interface WithdrawRepository {
    suspend fun withdraw(
        body: Double
    ): Response<Withdraw>
}
