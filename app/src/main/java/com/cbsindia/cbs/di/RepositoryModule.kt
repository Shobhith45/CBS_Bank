package com.cbsindia.cbs.di

import com.cbsindia.cbs.data.repository.base.CreateAccountRepository
import com.cbsindia.cbs.data.repository.impl.CreateAccountRepositoryImpl
import com.cbsindia.cbs.data.repository.base.*
import com.cbsindia.cbs.data.repository.impl.*
import com.cbsindia.cbs.data.repository.base.LoginRepository
import com.cbsindia.cbs.data.repository.base.BalanceRepository
import com.cbsindia.cbs.data.repository.base.MoneyTransferRepository
import com.cbsindia.cbs.data.repository.base.PrefRepository
import com.cbsindia.cbs.data.repository.base.TransactionRepository
import com.cbsindia.cbs.data.repository.impl.LoginRepositoryImpl
import com.cbsindia.cbs.data.repository.base.UserAccountRepository
import com.cbsindia.cbs.data.repository.impl.BalanceRepositoryImpl
import com.cbsindia.cbs.data.repository.impl.MoneyTransferRepositoryImpl
import com.cbsindia.cbs.data.repository.impl.PrefRepositoryImpl
import com.cbsindia.cbs.data.repository.impl.UserAccountRepositoryImpl
import com.cbsindia.cbs.data.repository.impl.TransactionRepositoryImpl
import org.koin.dsl.module

val repositoryModule = module {
    single<PrefRepository> {
        PrefRepositoryImpl()
    }
    factory<LoginRepository> {
        LoginRepositoryImpl(get())
    }
    factory<CreateAccountRepository> {
        CreateAccountRepositoryImpl(get())
    }
    factory<BalanceRepository> {
        BalanceRepositoryImpl(get())
    }
    factory<UserAccountRepository> {
        UserAccountRepositoryImpl()
    }
    factory<TransactionRepository> {
        TransactionRepositoryImpl(get())
    }
    factory<DepositRepository> {
        DepositRepositoryImpl(get())
    }
    factory<MoneyTransferRepository> {
        MoneyTransferRepositoryImpl(get())
    }
    factory<WithdrawRepository> {
        WithdrawRepositoryImpl(get())
    }
}
