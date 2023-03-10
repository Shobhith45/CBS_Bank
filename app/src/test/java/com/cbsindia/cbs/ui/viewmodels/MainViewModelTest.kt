package com.cbsindia.cbs.ui.viewmodels

import com.cbsindia.cbs.InstantExecutorListener
import com.cbsindia.cbs.data.models.Balance
import com.cbsindia.cbs.data.models.ResultInfo
import com.cbsindia.cbs.data.models.UserAccount
import com.cbsindia.cbs.data.remote.Status
import com.cbsindia.cbs.data.repository.base.BalanceRepository
import com.cbsindia.cbs.data.repository.base.UserAccountRepository
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
class MainViewModelTest : BehaviorSpec() {

    @MockK
    private lateinit var balanceRepository: BalanceRepository

    @MockK
    private lateinit var userAccountRepository: UserAccountRepository
    private lateinit var viewModel: MainViewModel

    init {
        listener(InstantExecutorListener())

        beforeTest {
            Dispatchers.setMain(UnconfinedTestDispatcher())
            MockKAnnotations.init(this)
            viewModel = MainViewModel(balanceRepository, userAccountRepository)
        }

        given("fetch balance") {
            `when`("success") {
                then("should return SUCCESS status") {
                    runTest {
                        coEvery { balanceRepository.getBalance() } returns
                                Response.success(
                                    200,
                                    Balance(1000.0, ResultInfo("success"))
                                )
                        val expectedResult = Status.SUCCESS
                        viewModel.getBalance()
                        val actualResult = viewModel.balanceLiveData.getOrAwaitValue().status
                        actualResult shouldBe expectedResult
                    }
                }
            }
            `when`("error") {
                then("should return ERROR status") {
                    runTest {
                        coEvery { balanceRepository.getBalance() } returns
                                Response.error(
                                    406,
                                    ResponseBody.create(
                                        MediaType.parse("application/json"),
                                        ""
                                    )
                                )
                        val expectedResult = Status.ERROR
                        viewModel.getBalance()
                        val actualResult = viewModel.balanceLiveData.getOrAwaitValue().status
                        actualResult shouldBe expectedResult
                    }
                }
            }
        }
        given("get user account") {
            `when`("success") {
                then("should return SUCCESS status") {
                    runTest {
                        coEvery { userAccountRepository.getUserAccount(any()) } returns
                                UserAccount("dinesh", 661234500006)
                        viewModel.getUserAccount()
                        val expectedResult = Status.SUCCESS
                        val actualResult = viewModel.userLiveData.getOrAwaitValue().status
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
