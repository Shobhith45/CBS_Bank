package com.cbsindia.cbs.data.repository.base

import com.cbsindia.cbs.data.models.Balance
import retrofit2.Response

interface BalanceRepository {
    suspend fun getBalance(): Response<Balance>
}
