package com.cbsindia.cbs.util

import androidx.lifecycle.MutableLiveData

object SessionManager {
    val isTokenExpired: MutableLiveData<Event<Boolean>> = MutableLiveData(Event(false))
    fun sessionExpired() {
        isTokenExpired.value = Event(true)
    }
}
