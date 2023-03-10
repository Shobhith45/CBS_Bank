package com.cbsindia.cbs.data.remote.datasource

import androidx.paging.PagingSource
import com.cbsindia.cbs.InstantExecutorListener
import com.cbsindia.cbs.data.models.ResultInfo
import com.cbsindia.cbs.data.models.Transaction
import com.cbsindia.cbs.data.models.TransactionResponse
import com.cbsindia.cbs.data.remote.TransactionApi
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest

@OptIn(ExperimentalCoroutinesApi::class)
class TransactionDataSourceTest : BehaviorSpec() {

    @MockK
    private lateinit var api:TransactionApi
    private lateinit var dataSource:TransactionDataSource
    private val transactionResponse = TransactionResponse(
        ResultInfo("message"),
        listOf(
            Transaction(1, "0", "0", 100.0, "credit", "10/03/2022")
        )
    )
    private val nextTransactionResponse = TransactionResponse(
        ResultInfo("message"),
        listOf(
            Transaction(2, "0", "0", 100.0, "credit", "10/03/2022")
        )
    )

    init {
        listener(InstantExecutorListener())

        beforeTest {
            MockKAnnotations.init(this)
            dataSource = TransactionDataSource(api)
        }

        given("transaction paging source load") {
            `when`("no items") {
                then("should return empty list") {
                    runTest {
                        coEvery { api.getTransactions(any(), any()) } returns TransactionResponse(
                            ResultInfo(""),
                            listOf()
                        )
                        val expected = PagingSource.LoadResult.Page<Int, Transaction>(
                            data = listOf(),
                            prevKey = null,
                            nextKey = 1
                        )
                        val actual = dataSource.load(
                            PagingSource.LoadParams.Refresh(
                                key = 0,
                                loadSize = 1,
                                placeholdersEnabled = false
                            )
                        )
                        actual shouldBe expected
                    }
                }
            }
            `when`("refresh paging source") {
                then("should return success") {
                    runTest {
                        coEvery { api.getTransactions(any(), any()) } returns transactionResponse
                        val expected = PagingSource.LoadResult.Page(
                            data = transactionResponse.transactions,
                            prevKey = null,
                            nextKey = 1
                        )
                        val actual = dataSource.load(
                            PagingSource.LoadParams.Refresh(
                                key = 0,
                                loadSize = 1,
                                placeholdersEnabled = false
                            )
                        )
                        actual shouldBe expected
                    }
                }
            }
            `when`("append paging source") {
                then("should return success") {
                    runTest {
                        coEvery {
                            api.getTransactions(
                                any(),
                                any()
                            )
                        } returns nextTransactionResponse
                        val expected = PagingSource.LoadResult.Page(
                            data = nextTransactionResponse.transactions,
                            prevKey = 0,
                            nextKey = 2
                        )
                        val actual = dataSource.load(
                            PagingSource.LoadParams.Append(
                                key = 1,
                                loadSize = 1,
                                placeholdersEnabled = false
                            )
                        )
                        actual shouldBe expected
                    }
                }
            }
            `when`("prepend paging source") {
                then("should return success") {
                    runTest {
                        coEvery { api.getTransactions(any(), any()) } returns transactionResponse
                        val expected = PagingSource.LoadResult.Page(
                            data = transactionResponse.transactions,
                            prevKey = null,
                            nextKey = 1
                        )
                        val actual = dataSource.load(
                            PagingSource.LoadParams.Append(
                                key = 0,
                                loadSize = 1,
                                placeholdersEnabled = false
                            )
                        )
                        actual shouldBe expected
                    }
                }
            }
        }
    }
}