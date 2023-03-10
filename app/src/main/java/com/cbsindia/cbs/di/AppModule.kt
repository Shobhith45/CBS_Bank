package com.cbsindia.cbs.di

import android.app.Application
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.cbsindia.cbs.util.Constant.PREF_FILE_KEY
import com.google.gson.Gson
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val appModule = module {
    single { getSharedPrefs(androidApplication()) }
    single<SharedPreferences.Editor> {
        getSharedPrefs(androidApplication()).edit()
    }
    single { Gson() }
}

private fun getSharedPrefs(androidApplication: Application): SharedPreferences =
    androidApplication.getSharedPreferences(PREF_FILE_KEY, MODE_PRIVATE)
