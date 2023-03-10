package com.cbsindia.cbs.data.repository.base

import com.cbsindia.cbs.data.models.CreateAccount
import retrofit2.Response

interface CreateAccountRepository {
    suspend fun createAccount(
        body: HashMap<String, String>
    ): Response<CreateAccount>
}
