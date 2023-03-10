package com.cbsindia.cbs.data.models

import com.google.gson.annotations.SerializedName

data class CustomerDetails(
    @SerializedName("customerNo")
    var customerNo: String?,
    @SerializedName("accountNumber")
    var accountNumber: Double?
)
