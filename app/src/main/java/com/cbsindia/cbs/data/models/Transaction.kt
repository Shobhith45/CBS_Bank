package com.cbsindia.cbs.data.models

import com.google.gson.annotations.SerializedName

data class TransactionResponse(
    val resultInfo: ResultInfo,
    @SerializedName("data")
    val transactions: List<Transaction>
)

data class Transaction(
    val transactionId: Int,
    val fromAccountNumber: String,
    val toAccountNumber: String,
    val amount: Double,
    val type: String,
    val dateAndTime: String
)

enum class TransactionType {
    CREDIT,
    DEBIT,
    MONEY_TRANSFER
}
