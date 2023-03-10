package com.cbsindia.cbs.data.repository.base

import com.cbsindia.cbs.data.models.UserAccount

interface UserAccountRepository {
    suspend fun getUserAccount(
        userPosition: Int
    ): UserAccount
}
