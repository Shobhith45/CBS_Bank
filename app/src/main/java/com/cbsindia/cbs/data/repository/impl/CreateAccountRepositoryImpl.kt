package com.cbsindia.cbs.data.repository.impl

import com.cbsindia.cbs.data.models.CreateAccount
import com.cbsindia.cbs.data.remote.CreateAccountApi
import com.cbsindia.cbs.data.repository.base.CreateAccountRepository
import retrofit2.Response

class CreateAccountRepositoryImpl(
    private val createAccountApi: CreateAccountApi
) : CreateAccountRepository {
    override suspend fun createAccount(
        body: HashMap<String, String>
    ): Response<CreateAccount> =
        createAccountApi.createAccount(body)
}
