package com.cbsindia.cbs.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.cbsindia.cbs.R
import com.cbsindia.cbs.data.models.Transaction
import com.cbsindia.cbs.data.models.TransactionType
import com.cbsindia.cbs.databinding.ItemTransactionBinding
import com.cbsindia.cbs.util.TextUtil

class TransactionAdapter() :
    PagingDataAdapter<Transaction, TransactionAdapter.TransactionViewHolder>(
        TransactionDiffCallback()
    ) {

    inner class TransactionViewHolder(
        private val binding:ItemTransactionBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(data:Transaction?) {
            if (data != null) {
                binding.apply {
                    transactionId.text =
                        TextUtil.addPaddingToTransactionId(data.transactionId)
                    time.text = data.dateAndTime
                    val type = TextUtil.getTransactionTypeEnum(data.type)
                    amount.text =
                        TextUtil.addCommaToBalance(data.amount)
                    val colorGreen = ContextCompat.getColor(amount.context, R.color.green)
                    val colorOrange = ContextCompat.getColor(amount.context, R.color.orange)
                    amount.setTextColor(
                        if (type == TransactionType.CREDIT) colorGreen else colorOrange
                    )
                }
            }
        }
    }

    override fun onBindViewHolder(holder:TransactionViewHolder, position:Int) {
        val data = getItem(position)
        holder.bind(data)
    }

    override fun onCreateViewHolder(parent:ViewGroup, viewType:Int):TransactionViewHolder {
        return TransactionViewHolder(
            ItemTransactionBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }
}

class TransactionDiffCallback : DiffUtil.ItemCallback<Transaction>() {
    override fun areItemsTheSame(oldItem:Transaction, newItem:Transaction):Boolean {
        return oldItem.transactionId == newItem.transactionId
    }

    override fun areContentsTheSame(oldItem:Transaction, newItem:Transaction):Boolean {
        return oldItem == newItem
    }
}
