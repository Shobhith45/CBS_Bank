package com.cbsindia.cbs.data.remote.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.cbsindia.cbs.data.models.Transaction
import com.cbsindia.cbs.data.remote.TransactionApi
import com.cbsindia.cbs.util.Constant.PAGE_NUMBER
import com.cbsindia.cbs.util.Constant.UNAUTHORIZED_ERROR_CODE
import com.cbsindia.cbs.util.SessionManager
import org.koin.core.component.KoinComponent
import retrofit2.HttpException
import java.io.IOException

class TransactionDataSource(
    private val transactionApi: TransactionApi,
) : PagingSource<Int, Transaction>(), KoinComponent {

    override fun getRefreshKey(state: PagingState<Int, Transaction>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Transaction> {
        val page = params.key ?: PAGE_NUMBER
        return try {
            val response = transactionApi.getTransactions(
                params.loadSize, page
            )
            val transactions = response.transactions
            LoadResult.Page(
                data = transactions,
                prevKey = if (page == 0) null else page - 1,
                nextKey = page + 1
            )
        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            if (exception.code() == UNAUTHORIZED_ERROR_CODE) {
                SessionManager.sessionExpired()
            }
            LoadResult.Error(exception)
        }
    }
}
