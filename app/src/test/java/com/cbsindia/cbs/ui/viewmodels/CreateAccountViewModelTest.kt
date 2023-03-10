package com.cbsindia.cbs.ui.viewmodels

import com.cbsindia.cbs.InstantExecutorListener
import com.cbsindia.cbs.data.models.CreateAccount
import com.cbsindia.cbs.data.models.CustomerDetails
import com.cbsindia.cbs.data.models.ResultInfo
import com.cbsindia.cbs.data.remote.Status
import com.cbsindia.cbs.data.repository.base.CreateAccountRepository
import com.cbsindia.cbs.getOrAwaitValue
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.setMain
import okhttp3.ResponseBody
import retrofit2.Response
import okhttp3.MediaType
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest

@OptIn(ExperimentalCoroutinesApi::class)
class CreateAccountViewModelTest : BehaviorSpec() {
    @MockK
    private lateinit var repository: CreateAccountRepository
    private lateinit var viewModel: CreateAccountViewModel
    init {
        listener(InstantExecutorListener())

        beforeTest {
            Dispatchers.setMain(UnconfinedTestDispatcher())
            MockKAnnotations.init(this)
            viewModel = CreateAccountViewModel(repository)
        }
        given("validate form") {
            `when`("Input Empty full name") {
                then("should return EmptyFullName event") {
                    viewModel.validateForm(
                        "",
                        "9901234567",
                        "367598566033",
                        "01031990",
                        "Jack",
                        "Father",
                        "123456",
                        "123456",
                        "udupi"
                    )
                    val expectedResult = CreateAccountEvent.EmptyFullName
                    val actualResult =
                        viewModel.formLiveData.getOrAwaitValue().getContentIfNotHandled()
                    actualResult shouldBe expectedResult
                }
            }
            `when`("Input Empty phone number ") {
                then("should return EmptyPhoneNumber event") {
                    viewModel.validateForm(
                        "Nikki",
                        "",
                        "367598566033",
                        "01031990",
                        "Jack",
                        "Father",
                        "123456",
                        "123456",
                        "udupi"
                    )
                    val expectedResult = CreateAccountEvent.EmptyPhoneNumber
                    val actualResult =
                        viewModel.formLiveData.getOrAwaitValue().getContentIfNotHandled()
                    actualResult shouldBe expectedResult
                }
            }
            `when`("Input phone number less than 10 digits") {
                then("should return LimitPhoneNumber event") {
                    viewModel.validateForm(
                        "Nikki",
                        "99",
                        "367598566033",
                        "01031990",
                        "Jack",
                        "Father",
                        "123456",
                        "123456",
                        "udupi"
                    )
                    val expectedResult = CreateAccountEvent.LimitMobileNumber
                    val actualResult =
                        viewModel.formLiveData.getOrAwaitValue().getContentIfNotHandled()
                    actualResult shouldBe expectedResult
                }
            }
            `when`("Input Empty UID Number") {
                then("should return EmptyUIDNumber event") {
                    viewModel.validateForm(
                        "Nikki",
                        "9901116545",
                        "",
                        "01031990",
                        "Jack",
                        "Father",
                        "123456",
                        "123456",
                        "udupi"
                    )
                    val expectedResult = CreateAccountEvent.EmptyUIDNumber
                    val actualResult =
                        viewModel.formLiveData.getOrAwaitValue().getContentIfNotHandled()
                    actualResult shouldBe expectedResult
                }
            }
            `when`("Input UID Number less than 12 digits") {
                then("should return LimitUIDNumber event") {
                    viewModel.validateForm(
                        "Nikki",
                        "9901116545",
                        "3675",
                        "01031990",
                        "Jack",
                        "Father",
                        "123456",
                        "123456",
                        "udupi"
                    )
                    val expectedResult = CreateAccountEvent.LimitAadhaarNumber
                    val actualResult =
                        viewModel.formLiveData.getOrAwaitValue().getContentIfNotHandled()
                    actualResult shouldBe expectedResult
                }
            }
            `when`("Input Empty BirthDate") {
                then("should return EmptyBirthDate event") {
                    viewModel.validateForm(
                        "Nikki",
                        "9078654378",
                        "367598566033",
                        "",
                        "Jack",
                        "Father",
                        "123456",
                        "123456",
                        "udupi"
                    )
                    val expectedResult = CreateAccountEvent.EmptyBirthDate
                    val actualResult =
                        viewModel.formLiveData.getOrAwaitValue().getContentIfNotHandled()
                    actualResult shouldBe expectedResult
                }
            }
            `when`("Input Empty parent name") {
                then("should return EmptyParentName event") {
                    viewModel.validateForm(
                        "Nikki",
                        "9078563445",
                        "367598566033",
                        "01031990",
                        "",
                        "Father",
                        "123456",
                        "123456",
                        "udupi"
                    )
                    val expectedResult = CreateAccountEvent.EmptyParentName
                    val actualResult =
                        viewModel.formLiveData.getOrAwaitValue().getContentIfNotHandled()
                    actualResult shouldBe expectedResult
                }
            }
            `when`("Input Empty nominee") {
                then("should return EmptyNominee event") {
                    viewModel.validateForm(
                        "Nikki",
                        "8765347856",
                        "367598566033",
                        "01031990",
                        "Jack",
                        "",
                        "123456",
                        "123456",
                        "udupi"
                    )
                    val expectedResult = CreateAccountEvent.EmptyNomineeName
                    val actualResult =
                        viewModel.formLiveData.getOrAwaitValue().getContentIfNotHandled()
                    actualResult shouldBe expectedResult
                }
            }
            `when`("Input Empty mpin") {
                then("should return Emptympin event") {
                    viewModel.validateForm(
                        "Nikki",
                        "7809674589",
                        "367598566033",
                        "01031990",
                        "Jack",
                        "Father",
                        "",
                        "123456",
                        "udupi"
                    )
                    val expectedResult = CreateAccountEvent.EmptyMPIN
                    val actualResult =
                        viewModel.formLiveData.getOrAwaitValue().getContentIfNotHandled()
                    actualResult shouldBe expectedResult
                }
            }
            `when`("Input  mpin less than 6 digits") {
                then("should return Limitmpin event") {
                    viewModel.validateForm(
                        "Nikki",
                        "7809674589",
                        "367598566033",
                        "01031990",
                        "Jack",
                        "Father",
                        "11",
                        "123456",
                        "udupi"
                    )
                    val expectedResult = CreateAccountEvent.LimitMPIN
                    val actualResult =
                        viewModel.formLiveData.getOrAwaitValue().getContentIfNotHandled()
                    actualResult shouldBe expectedResult
                }
            }
            `when`("Input Empty confirm mpin") {
                then("should return EmptyConfirmMPIN event") {
                    viewModel.validateForm(
                        "Nikki",
                        "9078569878",
                        "367598566033",
                        "01031990",
                        "Jack",
                        "Father",
                        "123456",
                        "",
                        "udupi"
                    )
                    val expectedResult = CreateAccountEvent.EmptyConfirmMPIN
                    val actualResult =
                        viewModel.formLiveData.getOrAwaitValue().getContentIfNotHandled()
                    actualResult shouldBe expectedResult
                }
            }
            `when`("Input mpin not equal to confirm mpin") {
                then("should return MpinNotEqualToConformMpin event") {
                    viewModel.validateForm(
                        "Nikki",
                        "9078569878",
                        "367598566033",
                        "01031990",
                        "Jack",
                        "Father",
                        "123456",
                        "11",
                        "udupi"
                    )
                    val expectedResult = CreateAccountEvent.MpinNotEqualsConfirmMpin
                    val actualResult =
                        viewModel.formLiveData.getOrAwaitValue().getContentIfNotHandled()
                    actualResult shouldBe expectedResult
                }
            }
            `when`("Input Empty address") {
                then("should return  EmptyAddress event") {
                    viewModel.validateForm(
                        "Nikki",
                        "9078987690",
                        "367598566033",
                        "01031990",
                        "Jack",
                        "Father",
                        "123456",
                        "123456",
                        ""
                    )
                    val expectedResult = CreateAccountEvent.EmptyAddress
                    val actualResult =
                        viewModel.formLiveData.getOrAwaitValue().getContentIfNotHandled()
                    actualResult shouldBe expectedResult
                }
            }
            `when`("Input All Valid") {
                then("should return  Success event") {
                    viewModel.validateForm(
                        "Nikki",
                        "9078987690",
                        "367598566033",
                        "01031990",
                        "Jack",
                        "Father",
                        "123456",
                        "123456",
                        "udupi"
                    )
                    val expectedResult = CreateAccountEvent.Success
                    val actualResult =
                        viewModel.formLiveData.getOrAwaitValue().getContentIfNotHandled()
                    actualResult shouldBe expectedResult
                }
            }
        }
        given("Create Account") {
            `when`("success") {
                then("should return status SUCCESS") {
                    runTest {
                        coEvery { repository.createAccount(any()) } returns Response.success(
                            200,
                            CreateAccount(
                                ResultInfo("success"),
                                CustomerDetails("101040", 661234500005.0)
                            )
                        )
                        viewModel.createAccount(
                            hashMapOf(
                                "name" to "Nikki",
                                "dob" to "01031990",
                                "address" to "udupi",
                                "mobileNumber" to "9078987690",
                                "mpin" to "123456",
                                "parentsName" to "Jack",
                                "nominee" to "Father",
                                "aadhaarNumber" to "367598566033"
                            )
                        )
                        val actualResult = viewModel.createAccountViewModel.getOrAwaitValue()
                            .getContentIfNotHandled()?.status
                        val expectedResult = Status.SUCCESS
                        actualResult shouldBe expectedResult
                    }
                }
            }
            `when`("error") {
                then("should return  status ERROR") {
                    runTest {
                        coEvery { repository.createAccount(any()) } returns
                                Response.error(
                                    400,
                                    ResponseBody.create(
                                        MediaType.parse("application/json"),
                                        ""
                                    )
                                )
                        viewModel.createAccount(
                            hashMapOf(
                                "name" to "Nikki",
                                "dob" to "01031990",
                                "address" to "udupi",
                                "mobileNumber" to "9078987690",
                                "mpin" to "123456",
                                "parentsName" to "Jack",
                                "nominee" to "Father",
                                "aadhaarNumber" to "367598566033"
                            )
                        )
                        val actualResult = viewModel.createAccountViewModel.getOrAwaitValue()
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
