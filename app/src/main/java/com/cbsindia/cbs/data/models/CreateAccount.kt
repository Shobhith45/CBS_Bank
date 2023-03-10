package com.cbsindia.cbs.data.models

import com.google.gson.annotations.SerializedName

data class CreateAccount(
    @SerializedName("resultInfo")
    var resultInfo: ResultInfo,
    @SerializedName("data")
    var customerDetails: CustomerDetails
)
