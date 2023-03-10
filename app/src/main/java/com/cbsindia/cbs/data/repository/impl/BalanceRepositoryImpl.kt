package com.cbsindia.cbs.data.repository.impl

import com.cbsindia.cbs.data.models.Balance
import com.cbsindia.cbs.data.remote.BalanceApi
import com.cbsindia.cbs.data.repository.base.BalanceRepository
import retrofit2.Response

class BalanceRepositoryImpl(
    private val balanceApi: BalanceApi
) : BalanceRepository {
    override suspend fun getBalance(): Response<Balance> =
        balanceApi.getBalance()
}
