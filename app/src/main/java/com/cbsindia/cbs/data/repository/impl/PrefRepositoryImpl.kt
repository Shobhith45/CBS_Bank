package com.cbsindia.cbs.data.repository.impl

import android.content.SharedPreferences
import com.cbsindia.cbs.data.repository.base.PrefRepository
import com.cbsindia.cbs.util.Constant.KEY_CUSTOMER_ID
import com.cbsindia.cbs.util.Constant.KEY_IS_INTRO_SHOWN
import com.cbsindia.cbs.util.Constant.KEY_IS_LOGGED_IN
import com.cbsindia.cbs.util.Constant.KEY_USER_MPIN
import com.cbsindia.cbs.util.Constant.KEY_USER_TOKEN
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class PrefRepositoryImpl : PrefRepository, KoinComponent {

    private val sharedPref: SharedPreferences by inject()
    private val sharedPrefEditor: SharedPreferences.Editor = sharedPref.edit()

    override fun setIsUserLoggedIn(isLoggedIn: Boolean) {
        sharedPrefEditor.apply {
            putBoolean(KEY_IS_LOGGED_IN, isLoggedIn)
            apply()
        }
    }

    override fun getIsUserLoggedIn(): Boolean =
        sharedPref.getBoolean(KEY_IS_LOGGED_IN, false)

    override fun setIsIntroScreenShown(isShown: Boolean) {
        sharedPrefEditor.apply {
            putBoolean(KEY_IS_INTRO_SHOWN, isShown)
            apply()
        }
    }

    override fun getIsIntroScreenShown(): Boolean =
        sharedPref.getBoolean(KEY_IS_INTRO_SHOWN, false)

    override fun setCustomerId(customerId: String) {
        sharedPrefEditor.apply {
            putString(KEY_CUSTOMER_ID, customerId)
            apply()
        }
    }

    override fun getCustomerId(): String? =
        sharedPref.getString(KEY_CUSTOMER_ID, null)

    override fun setUserToken(token: String) {
        sharedPrefEditor.apply {
            putString(KEY_USER_TOKEN, token)
            apply()
        }
    }

    override fun getUserToken(): String? =
        sharedPref.getString(KEY_USER_TOKEN, null)

    override fun setUserMpin(mPin: Long) {
        sharedPrefEditor.apply {
            putLong(KEY_USER_MPIN, mPin)
            apply()
        }
    }

    override fun getUserMpin(): Long =
        sharedPref.getLong(KEY_USER_MPIN, 0)
}
