package com.cbsindia.cbs.data.repository.impl

import com.cbsindia.cbs.data.models.Deposit
import com.cbsindia.cbs.data.remote.DepositApi
import com.cbsindia.cbs.data.repository.base.DepositRepository
import retrofit2.Response

class DepositRepositoryImpl(
    private val depositApi: DepositApi
) : DepositRepository {
    override suspend fun deposit(
        body: Double
    ): Response<Deposit> =
        depositApi.deposit(body)
}
