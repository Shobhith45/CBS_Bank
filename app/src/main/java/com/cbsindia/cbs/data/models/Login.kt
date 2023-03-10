package com.cbsindia.cbs.data.models

import com.google.gson.annotations.SerializedName

data class Login(
    @SerializedName("resultInfo")
    var resultInfo: ResultInfo,
    @SerializedName("data")
    var token: String
)
