package com.cbsindia.cbs.data.repository.impl

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.cbsindia.cbs.data.models.Transaction
import com.cbsindia.cbs.data.remote.TransactionApi
import com.cbsindia.cbs.data.remote.datasource.TransactionDataSource
import com.cbsindia.cbs.data.repository.base.TransactionRepository
import com.cbsindia.cbs.util.Constant.PAGE_SIZE

class TransactionRepositoryImpl(
    private val transactionApi:TransactionApi
) : TransactionRepository {

    override fun getTransactions():LiveData<PagingData<Transaction>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { TransactionDataSource(transactionApi) }
        ).liveData
    }
}
