package com.cbsindia.cbs.data.repository.impl

import com.cbsindia.cbs.data.models.MoneyTransfer
import com.cbsindia.cbs.data.models.ResultInfo
import com.cbsindia.cbs.data.remote.MoneyTransferApi
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType
import okhttp3.ResponseBody
import retrofit2.Response

@OptIn(ExperimentalCoroutinesApi::class)
class MoneyTransferRepositoryImplTest : BehaviorSpec() {

    @MockK
    private lateinit var api: MoneyTransferApi
    private lateinit var repository: MoneyTransferRepositoryImpl

    init {
        beforeTest {
            MockKAnnotations.init(this)
            repository = MoneyTransferRepositoryImpl(api)
        }

        given("money transfer") {
            `when`("Success") {
                then("should return success response") {
                    runTest {
                        val accountNumber = 661234500006
                        val amount = 300.0
                        val body = hashMapOf<String, Any>(
                            "toAccountNumber" to accountNumber,
                            "amount" to amount
                        )
                        coEvery { api.transferMoney(body) } returns
                                Response.success(
                                    200,
                                    MoneyTransfer(1, ResultInfo("SUCCESS"))
                                )
                        val resp = repository.transferMoney(body)
                        resp.isSuccessful.shouldBeTrue()
                    }
                }
                `when`("invalid account number") {
                    then("should return error response") {
                        runTest {
                            val accountNumber = 10000000000
                            val amount = 200.0
                            val body = hashMapOf<String, Any>(
                                "toAccountNumber" to accountNumber,
                                "amount" to amount
                            )
                            coEvery { api.transferMoney(body) } returns
                                    Response.error(
                                        406,
                                        ResponseBody.create(
                                            MediaType.parse("application/json"),
                                            ""
                                        )
                                    )
                            val resp = repository.transferMoney(body)
                            resp.isSuccessful.shouldBeFalse()
                        }
                    }
                }
                `when`("insufficient balance") {
                    then("should return error response") {
                        runTest {
                            val accountNumber = 661234500006
                            val amount = 700.0
                            val body = hashMapOf<String, Any>(
                                "toAccountNumber" to accountNumber,
                                "amount" to amount
                            )
                            coEvery { api.transferMoney(body) } returns
                                    Response.error(
                                        406,
                                        ResponseBody.create(
                                            MediaType.parse("application/json"),
                                            ""
                                        )
                                    )
                            val resp = repository.transferMoney(body)
                            resp.isSuccessful.shouldBeFalse()
                        }
                    }
                }
            }
        }
    }
}
