package com.cbsindia.cbs.di

import com.cbsindia.cbs.ui.viewmodels.DepositViewModel
import com.cbsindia.cbs.ui.viewmodels.MainViewModel
import com.cbsindia.cbs.ui.viewmodels.WelcomeViewModel
import com.cbsindia.cbs.ui.viewmodels.LoginViewModel
import com.cbsindia.cbs.ui.viewmodels.CreateAccountViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import com.cbsindia.cbs.ui.viewmodels.MoneyTransferViewModel
import org.koin.dsl.module
import com.cbsindia.cbs.ui.viewmodels.TransactionViewModel
import com.cbsindia.cbs.ui.viewmodels.WithdrawViewModel

val viewModelModule = module {
    viewModel {
        LoginViewModel(get())
    }
    viewModel {
        CreateAccountViewModel(get())
    }
    viewModel {
        WelcomeViewModel()
    }
    viewModel {
        MainViewModel(get(), get())
    }
    viewModel {
        TransactionViewModel(get())
    }
    viewModel {
        DepositViewModel(get())
    }
    viewModel {
        MoneyTransferViewModel(get())
    }
    viewModel {
        WithdrawViewModel(get())
    }
}
