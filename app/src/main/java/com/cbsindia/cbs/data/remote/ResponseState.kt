package com.cbsindia.cbs.data.remote

data class ResponseState<out T>(val status: Status, val data: T?, val message: String?) {
    companion object {
        fun <T> success(data: T?): ResponseState<T> {
            return ResponseState(Status.SUCCESS, data, null)
        }

        fun <T> error(msg: String): ResponseState<T> {
            return ResponseState(Status.ERROR, null, msg)
        }

        fun <T> loading(): ResponseState<T> {
            return ResponseState(Status.LOADING,null, null)
        }
    }
}
enum class Status{
    SUCCESS,
    ERROR,
    LOADING
}
