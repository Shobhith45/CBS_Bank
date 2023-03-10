package com.cbsindia.cbs.data.repository.impl

import com.cbsindia.cbs.data.models.MoneyTransfer
import com.cbsindia.cbs.data.remote.MoneyTransferApi
import com.cbsindia.cbs.data.repository.base.MoneyTransferRepository
import retrofit2.Response

class MoneyTransferRepositoryImpl(
    private val moneyTransferApi: MoneyTransferApi
) : MoneyTransferRepository {
    override suspend fun transferMoney(
        body: HashMap<String, Any>
    ): Response<MoneyTransfer> =
        moneyTransferApi.transferMoney(body)
}
