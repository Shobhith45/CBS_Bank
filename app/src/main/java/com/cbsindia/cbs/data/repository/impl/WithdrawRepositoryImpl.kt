package com.cbsindia.cbs.data.repository.impl

import com.cbsindia.cbs.data.models.Withdraw
import com.cbsindia.cbs.data.remote.WithdrawApi
import com.cbsindia.cbs.data.repository.base.WithdrawRepository
import retrofit2.Response

class WithdrawRepositoryImpl(
    private val withdrawApi: WithdrawApi
) : WithdrawRepository {
    override suspend fun withdraw(
        body: Double
    ): Response<Withdraw> =
        withdrawApi.withdraw(body)
}
