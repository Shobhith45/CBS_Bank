package com.cbsindia.cbs.data.repository.impl

import com.cbsindia.cbs.data.remote.CreateAccountApi
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import org.mockserver.model.HttpRequest
import org.mockserver.model.HttpResponse
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.mockserver.client.MockServerClient
import org.mockserver.integration.ClientAndServer
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@OptIn(ExperimentalCoroutinesApi::class)
class CreateAccountRepositoryImplTest :  BehaviorSpec(){
    private val port = 10880
    private val retrofit = Retrofit
        .Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl("http://localhost:$port")
        .build()
    private val api = retrofit.create(CreateAccountApi::class.java)
    private lateinit var repository: CreateAccountRepositoryImpl
    private var mockServer: MockServerClient = MockServerClient("localhost", port)
    private val existingMobileNumber = "9908897689"
    init {
        beforeEach {
            repository = CreateAccountRepositoryImpl(api)
            mockServer = ClientAndServer.startClientAndServer(port)
        }
        given("Create Account") {
            `when`("Success") {
                then("should return success") {
                    val name = "trail"
                    val dob = "15101999"
                    val address = "Udupi"
                    val mobileNumber = "9987012312"
                    val mpin = "123123"
                    val parentsName = "Jack"
                    val nominee = "Father"
                    val aadhaarNumber = "6598 0978 6040"
                    runTest {
                        MockServerClient("localhost", port).`when`(
                            HttpRequest.request()
                                .withMethod("POST")
                                .withPath("/bank/register")
                        ).respond(
                            HttpResponse.response()
                                .withStatusCode(200)
                                .withBody("""{"resultInfo": {"message": "SOME_MESSAGE"}, "data":{"customerNo":"101045","accountNumber":661234500046}}""")
                        )
                        val body = hashMapOf(
                            "name" to name,
                            "dob" to dob,
                            "address" to address,
                            "mobileNumber" to mobileNumber,
                            "mpin" to mpin,
                            "parentsName" to parentsName,
                            "nominee" to nominee,
                            "aadhaarNumber" to aadhaarNumber
                        )
                        val resp = repository.createAccount(body).isSuccessful
                        resp.shouldBeTrue()
                    }
                }
            }
            `when`("Error") {
                then("should return error response") {
                    val name = "Nikki"
                    val dob = "15101999"
                    val address = "Udupi"
                    val mobileNumber = existingMobileNumber
                    val mpin = "123123"
                    val parentsName = "Jack"
                    val nominee = "Father"
                    val aadhaarNumber = "6598 0978 6034"
                    runTest {
                        MockServerClient("localhost", port).`when`(
                            HttpRequest.request()
                                .withMethod("POST")
                                .withPath("/bank/register")
                        ).respond(
                            HttpResponse.response()
                                .withStatusCode(406)
                                .withBody(
                                    """{"resultInfo": {  "message": "ERROR" },"data": null }"""
                                )
                        )
                        val body = hashMapOf(
                            "name" to name,
                            "dob" to dob,
                            "address" to address,
                            "mobileNumber" to mobileNumber,
                            "mpin" to mpin,
                            "parentsName" to parentsName,
                            "nominee" to nominee,
                            "aadhaarNumber" to aadhaarNumber
                        )
                        val resp = repository.createAccount(body).isSuccessful
                        resp.shouldBeFalse()
                    }
                }
            }
        }
        afterEach {
            mockServer.close()
        }
    }
}
