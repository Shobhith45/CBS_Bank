package com.cbsindia.cbs.util

object Constant {
    const val BASE_URL = "https://bankingsystems-api.herokuapp.com/"

    const val PREF_FILE_KEY = "com.cbsindia.cbs.PREFERENCE_FILE_KEY"
    const val KEY_IS_LOGGED_IN = "IS_LOGGED_IN"
    const val KEY_CUSTOMER_ID = "CUSTOMER_ID"
    const val KEY_USER_TOKEN = "USER_TOKEN"
    const val KEY_USER_MPIN = "USER_MPIN"
    const val KEY_IS_INTRO_SHOWN = "IS_INTRO_SHOWN"
    const val ENDPOINT_CREATE_ACCOUNT = "bank/register"
    const val KEY_DIALOG_TITLE = "TITLE"
    const val KEY_DIALOG_DESCRIPTION = "DESCRIPTION"
    const val KEY_AUTHORIZATION_HEADER = "Authorization"
    const val END_POINT_MONEY_TRANSFER = "transaction/account/transfer"
    const val ENDPOINT_BALANCE = "transaction/get/balance"
    const val TEXT_INR = "INR"
    const val API_SUCCESS = "SUCCESS"
    const val TEXT_RUPEES = "₹"
    const val KEY_PAGE_SIZE = "pageSize"
    const val KEY_PAGE_NUMBER = "pageNumber"
    const val END_POINT_TRANSACTION = "transaction/account/statement"
    const val PAGE_SIZE = 20
    const val PAGE_NUMBER = 0
    const val END_POINT_DEPOSIT = "transaction/credit"
    const val KEY_HEADER_CUSTID = "customerID"
    const val KEY_HEADER_MPIN = "mpin"
    const val END_POINT_LOGIN = "bank/authenticate"
    const val KEY_BEARER = "Bearer"
    const val END_POINT_WITHDRAW = "transaction/debit"
    const val MAX_AMOUNT = 50000.0
    const val MIN_AMOUNT = 0.0
    const val UNAUTHORIZED_ERROR_CODE = 401
    const val API_ERROR = "Try Again After Some Time"
    const val MPIN_LENGTH = 6
}
