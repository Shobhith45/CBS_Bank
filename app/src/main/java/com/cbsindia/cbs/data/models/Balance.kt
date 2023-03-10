package com.cbsindia.cbs.data.models

import com.google.gson.annotations.SerializedName

data class Balance(
    @SerializedName("data")
    val balance: Double,
    val resultInfo: ResultInfo
)
