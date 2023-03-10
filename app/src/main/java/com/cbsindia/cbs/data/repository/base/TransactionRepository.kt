package com.cbsindia.cbs.data.repository.base

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.cbsindia.cbs.data.models.Transaction

interface TransactionRepository {
    fun getTransactions():
            LiveData<PagingData<Transaction>>
}
