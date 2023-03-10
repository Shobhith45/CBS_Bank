package com.cbsindia.cbs.data.repository.base

import com.cbsindia.cbs.data.models.MoneyTransfer
import retrofit2.Response

interface MoneyTransferRepository {
    suspend fun transferMoney(
        body: HashMap<String, Any>
    ): Response<MoneyTransfer>
}
