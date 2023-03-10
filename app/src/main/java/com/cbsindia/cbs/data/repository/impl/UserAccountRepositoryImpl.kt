package com.cbsindia.cbs.data.repository.impl

import com.cbsindia.cbs.data.models.UserAccount
import com.cbsindia.cbs.data.repository.base.UserAccountRepository

class UserAccountRepositoryImpl : UserAccountRepository {

    private val userList = listOf(
        UserAccount("John Doe", 661234500006),
        UserAccount("Dinesh Gupta", 661234500007),
        UserAccount("Ramesh D", 661234500008)
    )

    override suspend fun getUserAccount(userPosition: Int): UserAccount =
        userList[userPosition]
}
