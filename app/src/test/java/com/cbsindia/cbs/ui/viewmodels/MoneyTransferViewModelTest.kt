package com.cbsindia.cbs.ui.viewmodels

import com.cbsindia.cbs.InstantExecutorListener
import com.cbsindia.cbs.data.models.MoneyTransfer
import com.cbsindia.cbs.data.models.ResultInfo
import com.cbsindia.cbs.data.remote.Status
import com.cbsindia.cbs.data.repository.base.MoneyTransferRepository
import com.cbsindia.cbs.getOrAwaitValue
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import okhttp3.MediaType
import okhttp3.ResponseBody
import retrofit2.Response

@OptIn(ExperimentalCoroutinesApi::class)
class MoneyTransferViewModelTest : BehaviorSpec() {

    @MockK
    private lateinit var repository: MoneyTransferRepository
    private lateinit var viewModel: MoneyTransferViewModel

    init {
        listener(InstantExecutorListener())

        beforeTest {
            Dispatchers.setMain(UnconfinedTestDispatcher())
            MockKAnnotations.init(this)
            viewModel = MoneyTransferViewModel(repository)
        }

        given("validate form") {
            `when`("Input Empty Amount") {
                then("should return EmptyAmount event") {
                    viewModel.validateForm("", "661234500067", "123456", 123456, 200.0)
                    val expectedResult = MoneyTransferEvent.EmptyAmount
                    val actualResult =
                        viewModel.formLiveData.getOrAwaitValue().getContentIfNotHandled()
                    actualResult shouldBe expectedResult
                }
            }
            `when`("Input Amount Zero") {
                then("should return ZeroAmount event") {
                    viewModel.validateForm("0", "661234500067", "123456", 123456, 200.0)
                    val expectedResult = MoneyTransferEvent.ZeroAmount
                    val actualResult =
                        viewModel.formLiveData.getOrAwaitValue().getContentIfNotHandled()
                    actualResult shouldBe expectedResult
                }
            }
            `when`("Input Empty Account Number") {
                then("should return EmptyAccountNumber event") {
                    viewModel.validateForm("100", "", "123456", 123456, 200.00)
                    val expectedResult = MoneyTransferEvent.EmptyAccountNumber
                    val actualResult =
                        viewModel.formLiveData.getOrAwaitValue().getContentIfNotHandled()
                    actualResult shouldBe expectedResult
                }
            }
            `when`("Input Empty MPin") {
                then("should return EmptyMpin event") {
                    viewModel.validateForm("100", "661234500067", "", 123456, 200.00)
                    val expectedResult = MoneyTransferEvent.EmptyMpin
                    val actualResult =
                        viewModel.formLiveData.getOrAwaitValue().getContentIfNotHandled()
                    actualResult shouldBe expectedResult
                }
            }
            `when`("Input MPin length less than 6") {
                then("should return LessMpinLength event") {
                    viewModel.validateForm("100", "661234500067", "123", 123456, 200.00)
                    val expectedResult = MoneyTransferEvent.LessMpinLength
                    val actualResult =
                        viewModel.formLiveData.getOrAwaitValue().getContentIfNotHandled()
                    actualResult shouldBe expectedResult
                }
            }
            `when`("Entered MPin not matches with Saved MPin") {
                then("should return LessMpinLength event") {
                    viewModel.validateForm("100", "661234500067", "985432", 123456, 200.00)
                    val expectedResult = MoneyTransferEvent.MpinNotMatching
                    val actualResult =
                        viewModel.formLiveData.getOrAwaitValue().getContentIfNotHandled()
                    actualResult shouldBe expectedResult
                }
            }
            `when`("Large Amount Input") {
                then("should return LargeAmount event") {
                    viewModel.validateForm("60000", "661234500067", "123456", 123456, 200.0)
                    val expectedResult = MoneyTransferEvent.LargeAmount
                    val actualResult =
                        viewModel.formLiveData.getOrAwaitValue().getContentIfNotHandled()
                    actualResult shouldBe expectedResult
                }
            }
            `when`("Input Amount Greater than Balance") {
                then("should return LowBalance event") {
                    viewModel.validateForm("400", "661234500067", "123456", 123456, 200.0)
                    val expectedResult = MoneyTransferEvent.LowBalance
                    val actualResult =
                        viewModel.formLiveData.getOrAwaitValue().getContentIfNotHandled()
                    actualResult shouldBe expectedResult
                }
            }
            `when`("All Valid Input") {
                then("should return Success event") {
                    viewModel.validateForm("100", "661234500067", "123456", 123456, 200.0)
                    val expectedResult = MoneyTransferEvent.Success
                    val actualResult =
                        viewModel.formLiveData.getOrAwaitValue().getContentIfNotHandled()
                    actualResult shouldBe expectedResult
                }
            }
        }

        given("Money Transfer") {
            `when`("Success") {
                then("should return SUCCESS status") {
                    runTest {
                        coEvery { repository.transferMoney(any()) } returns
                                Response.success(
                                    200,
                                    MoneyTransfer(1, ResultInfo("success"))
                                )
                        viewModel.transferMoney(
                            hashMapOf(
                                "toAccountNumber" to 661234500006,
                                "amount" to 100.0
                            )
                        )
                        val actualResult = viewModel.transferViewModel.getOrAwaitValue()
                            .getContentIfNotHandled()?.status
                        val expectedResult = Status.SUCCESS
                        actualResult shouldBe expectedResult
                    }
                }
            }
            `when`("Error") {
                then("should return ERROR status") {
                    runTest {
                        coEvery { repository.transferMoney(any()) } returns
                                Response.error(
                                    406,
                                    ResponseBody.create(
                                        MediaType.parse("application/json"),
                                        ""
                                    )
                                )
                        viewModel.transferMoney(
                            hashMapOf(
                                "toAccountNumber" to 6612345111999,
                                "amount" to 100.0
                            )
                        )
                        val actualResult = viewModel.transferViewModel.getOrAwaitValue()
                            .getContentIfNotHandled()?.status
                        val expectedResult = Status.ERROR
                        actualResult shouldBe expectedResult
                    }
                }
            }
        }
        afterTest {
            Dispatchers.resetMain()
        }
    }
}
