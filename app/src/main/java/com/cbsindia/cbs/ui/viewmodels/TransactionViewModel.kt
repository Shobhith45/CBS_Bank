package com.cbsindia.cbs.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.cbsindia.cbs.data.models.Transaction
import com.cbsindia.cbs.data.repository.base.TransactionRepository

class TransactionViewModel(
    private val transactionRepository:TransactionRepository
) : ViewModel() {

    var transactionLiveData:
            LiveData<PagingData<Transaction>> = MutableLiveData()

    fun getTransaction() {
        val transactions = transactionRepository.getTransactions().cachedIn(viewModelScope)
        transactionLiveData = transactions
    }
}
