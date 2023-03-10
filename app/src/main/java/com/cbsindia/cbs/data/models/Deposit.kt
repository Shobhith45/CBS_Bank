package com.cbsindia.cbs.data.models

import com.google.gson.annotations.SerializedName

data class Deposit(
    @SerializedName("data")
    val transactionId: Int,
    val resultInfo: ResultInfo
)
