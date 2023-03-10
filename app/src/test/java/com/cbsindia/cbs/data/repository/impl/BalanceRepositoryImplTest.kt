package com.cbsindia.cbs.data.repository.impl

import com.cbsindia.cbs.data.models.Balance
import com.cbsindia.cbs.data.models.ResultInfo
import com.cbsindia.cbs.data.remote.BalanceApi
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.shouldBe
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType
import okhttp3.ResponseBody
import retrofit2.Response

@OptIn(ExperimentalCoroutinesApi::class)
class BalanceRepositoryImplTest : BehaviorSpec() {

    @MockK
    private lateinit var api: BalanceApi
    private lateinit var repository: BalanceRepositoryImpl

    init {
        beforeTest {
            MockKAnnotations.init(this)
            repository = BalanceRepositoryImpl(api)
        }

        given("fetch balance") {
            `when`("success") {
                then("should return SUCCESS response") {
                    runTest {
                        coEvery { api.getBalance() } returns
                                Response.success(
                                    200,
                                    Balance(3000.0, ResultInfo("SUCCESS"))
                                )
                        val actual = repository.getBalance().body()
                        val expected = Balance(3000.0, ResultInfo("SUCCESS"))
                        actual shouldBe expected
                    }
                }
            }
            `when`("error") {
                then("should return ERROR response") {
                    runTest {
                        coEvery { api.getBalance() } returns
                                Response.error(
                                    406,
                                    ResponseBody.create(
                                        MediaType.parse("application/json"),
                                        ""
                                    )
                                )
                        val resp = repository.getBalance()
                        resp.isSuccessful.shouldBeFalse()
                    }
                }
            }
        }
    }
}
