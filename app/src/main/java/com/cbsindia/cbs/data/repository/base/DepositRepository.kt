package com.cbsindia.cbs.data.repository.base

import com.cbsindia.cbs.data.models.Deposit
import retrofit2.Response

interface DepositRepository {
    suspend fun deposit(
        body: Double
    ): Response<Deposit>
}
