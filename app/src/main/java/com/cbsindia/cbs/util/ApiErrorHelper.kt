package com.cbsindia.cbs.util

import com.cbsindia.cbs.util.Constant.API_ERROR

object ApiErrorHelper {
    fun getErrorMessage(message: String): String {
        return when (message) {
            "INSUFFICIENT_FUNDS" -> "Insufficient Balance"
            "INVALID_ACCOUNT_NUMBER" -> "Invalid Account Number"
            "DUPLICATE_ACCOUNT" -> "Mobile Number Already Exists"
            "INVALID_MOBILE_NUMBER" -> "Invalid Mobile Number"
            "INVALID MPIN" -> "Invalid MPIN"
            "INVALID_AADHAAR_NUMBER" -> "Invalid Aadhaar Number"
            "DUPLICATE_AADHAAR" -> "Aadhaar number already registered"
            "INVALID CREDENTIAL" -> "Invalid Credentials"
            else -> API_ERROR
        }
    }
}
