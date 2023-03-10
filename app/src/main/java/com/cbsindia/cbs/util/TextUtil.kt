package com.cbsindia.cbs.util

import com.cbsindia.cbs.data.models.TransactionType
import java.text.NumberFormat
import java.util.Locale

object TextUtil {
    fun addPaddingToTransactionId(transactionId: Int) =
        transactionId.toString().padStart(
            8,
            '0'
        )

    fun getTransactionTypeEnum(type: String) =
        if (type == "CREDIT")
            TransactionType.CREDIT
        else
            TransactionType.DEBIT

    fun addCommaToBalance(amount: Double): String {
        val format = NumberFormat.getCurrencyInstance(Locale("en", "in"))
        return format.format(amount)
    }
}
