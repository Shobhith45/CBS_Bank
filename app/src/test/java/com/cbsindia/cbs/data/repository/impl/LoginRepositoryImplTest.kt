package com.cbsindia.cbs.data.repository.impl

import com.cbsindia.cbs.data.models.Login
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.ExperimentalCoroutinesApi
import com.cbsindia.cbs.data.models.ResultInfo
import com.cbsindia.cbs.data.remote.LoginApi
import io.kotest.matchers.booleans.shouldBeFalse
import org.mockserver.client.MockServerClient
import org.mockserver.integration.ClientAndServer
import retrofit2.Retrofit
import kotlinx.coroutines.test.runTest
import retrofit2.converter.gson.GsonConverterFactory
import org.mockserver.model.HttpRequest
import org.mockserver.model.HttpResponse

@OptIn(ExperimentalCoroutinesApi::class)
class LoginRepositoryImplTest :  BehaviorSpec(){
    private val port = 10880
    private val retrofit = Retrofit
        .Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl("http://localhost:$port")
        .build()
    private val api = retrofit.create(LoginApi::class.java)
    private lateinit var repository: LoginRepositoryImpl
    private var mockServer: MockServerClient = MockServerClient("localhost", port)
    init {
        beforeEach {
            repository = LoginRepositoryImpl(api)
            mockServer = ClientAndServer.startClientAndServer(port)
        }
        given("money transfer") {
            `when`("Success") {
                then("should return success response") {
                    runTest {
                        MockServerClient("localhost", port).`when`(
                            HttpRequest.request()
                                .withMethod("POST")
                                .withPath("/bank/authenticate")
                                .withHeader("customerID", "101041")
                                .withHeader("mpin", "112233")

                        ).respond(
                            HttpResponse.response()
                                .withStatusCode(200)
                                .withBody(
                                    """{"resultInfo": {"message": "SUCCESS"}, "data": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxMDEwNDEiLCJleHAiOjE2NTMyNDM0NjEsImlhdCI6MTY1MzIyNTQ2MX0.Ic7iwtZfestO_10CyxAFA-QGgz9oE4w3GhYUeWsqhrKTdd31eI_dxOLM2uumJCHMXDRbS-kHrxU1a21PFJxJPw"}"""
                                )
                        )
                        val actual = repository.login("101041", "112233").body()
                        val expected = Login(
                            ResultInfo("SUCCESS"),
                            "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxMDEwNDEiLCJleHAiOjE2NTMyNDM0NjEsImlhdCI6MTY1MzIyNTQ2MX0.Ic7iwtZfestO_10CyxAFA-QGgz9oE4w3GhYUeWsqhrKTdd31eI_dxOLM2uumJCHMXDRbS-kHrxU1a21PFJxJPw"
                        )
                        actual shouldBe expected
                    }
                }
            }
            `when`("Success") {
                then("should return  error response") {
                    runTest {
                        MockServerClient("localhost", port).`when`(
                            HttpRequest.request()
                                .withMethod("POST")
                                .withPath("/bank/authenticate")
                                .withHeader("customerID", "1010")
                                .withHeader("mpin", "1122")
                        ).respond(
                            HttpResponse.response()
                                .withStatusCode(406)
                                .withBody(
                                    """{"resultInfo": {"message": "INVALID CREDENTIAL"}, "data": null}"""
                                )
                        )
                        val resp = repository.login("1010", "1122")
                        resp.isSuccessful.shouldBeFalse()
                    }
                }
            }
        }
        afterEach {
            mockServer.close()
        }
    }
}
