package com.cbsindia.cbs.data.remote

import com.cbsindia.cbs.data.models.Login
import com.cbsindia.cbs.util.Constant.END_POINT_LOGIN
import com.cbsindia.cbs.util.Constant.KEY_HEADER_CUSTID
import com.cbsindia.cbs.util.Constant.KEY_HEADER_MPIN
import retrofit2.Response
import retrofit2.http.Header
import retrofit2.http.POST

interface LoginApi {
    @POST(END_POINT_LOGIN)
    suspend fun login(
        @Header(KEY_HEADER_CUSTID) customerID: String,
        @Header(KEY_HEADER_MPIN) mPin: String
    ): Response<Login>
}
