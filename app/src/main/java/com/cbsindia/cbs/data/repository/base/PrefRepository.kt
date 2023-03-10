package com.cbsindia.cbs.data.repository.base

interface PrefRepository {
    fun setIsUserLoggedIn(isLoggedIn: Boolean)
    fun getIsUserLoggedIn(): Boolean
    fun setIsIntroScreenShown(isShown: Boolean)
    fun getIsIntroScreenShown(): Boolean
    fun setCustomerId(customerId: String)
    fun getCustomerId(): String?
    fun setUserToken(token: String)
    fun getUserToken(): String?
    fun setUserMpin(mPin: Long)
    fun getUserMpin(): Long
}
