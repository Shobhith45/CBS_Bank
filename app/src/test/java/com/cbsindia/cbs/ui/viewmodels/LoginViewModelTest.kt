package com.cbsindia.cbs.ui.viewmodels

import com.cbsindia.cbs.InstantExecutorListener
import com.cbsindia.cbs.data.models.Login
import com.cbsindia.cbs.data.models.ResultInfo
import com.cbsindia.cbs.data.remote.Status
import com.cbsindia.cbs.data.repository.base.LoginRepository
import com.cbsindia.cbs.getOrAwaitValue
import io.kotest.core.spec.style.BehaviorSpec
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody
import retrofit2.Response
import okhttp3.MediaType
import io.kotest.matchers.shouldBe
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.setMain

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest : BehaviorSpec() {
    @MockK
    private lateinit var repository: LoginRepository
    private lateinit var viewModel: LoginViewModel
    private val savedMpin="123456"
    init {
        listener(InstantExecutorListener())
        beforeTest {
            Dispatchers.setMain(UnconfinedTestDispatcher())
            MockKAnnotations.init(this)
            viewModel = LoginViewModel(repository)
        }
        given("validate form") {
            `when`("Input Empty customer id") {
                then("should return EmptyCustomerId event") {
                    viewModel.validateForm("", "100100",savedMpin,false)
                    val expected = LoginEvent.EmptyCustomerId
                    val actual = viewModel.formLiveData.getOrAwaitValue().getContentIfNotHandled()
                    actual shouldBe expected
                }
            }
            `when`("Input Empty mpin ") {
                then("should returnEmptyMPIN event") {
                    viewModel.validateForm("101014", "",savedMpin,false)
                    val expected = LoginEvent.EmptyMpin
                    val actual = viewModel.formLiveData.getOrAwaitValue().getContentIfNotHandled()
                    actual shouldBe expected
                }
            }
            `when`("Input user logged in and saved mpin not equal to mpin ") {
                then("should returnEmptyMPIN event") {
                    viewModel.validateForm("101014", "111111",savedMpin,true)
                    val expected = LoginEvent.ValidMPin
                    val actual = viewModel.formLiveData.getOrAwaitValue().getContentIfNotHandled()
                    actual shouldBe expected
                }
            }
            `when`("Input All Valid ") {
                then("should return Success event") {
                    viewModel.validateForm("100010", "123456",savedMpin,true)
                    val expected = LoginEvent.Success
                    val actual = viewModel.formLiveData.getOrAwaitValue().getContentIfNotHandled()
                    actual shouldBe expected
                }
            }
        }
        given("Login") {
            `when`("success api response") {
                then("should return status SUCCESS") {
                    runTest {
                        coEvery {
                            repository.login(
                                String(),
                                String()
                            )
                        } returns Response.success(
                            200,
                            Login(ResultInfo("success"), "")
                        )
                        viewModel.login(String(), String())
                        val actual = viewModel.loginViewModel.getOrAwaitValue().getContentIfNotHandled()?.status
                        val expected = Status.SUCCESS
                        actual shouldBe expected
                    }
                }
            }
            `when`("Error") {
                then("should return status ERROR") {
                    runTest {
                        coEvery { repository.login(String(), String()) } returns
                                Response.error(
                                    400,
                                    ResponseBody.create(
                                        MediaType.parse("application/json"),
                                        ""
                                    )
                                )
                        viewModel.login(String(), String())
                        val actual = viewModel.loginViewModel.getOrAwaitValue().getContentIfNotHandled()?.status
                        val expected = Status.ERROR
                        actual shouldBe expected
                    }
                }
            }
        }
    }
}
