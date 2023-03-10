package com.cbsindia.cbs.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.cbsindia.cbs.databinding.FragmentTransactionBinding
import com.cbsindia.cbs.ui.adapter.TransactionAdapter
import com.cbsindia.cbs.ui.viewmodels.TransactionViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class TransactionFragment : BaseFragment<FragmentTransactionBinding>() {

    private val transactionViewModel: TransactionViewModel by viewModel()
    private val transactionAdapter = TransactionAdapter()
    private val recyclerLayoutManager = LinearLayoutManager(context)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapter()
        setupViewClickListener()
        getTransactions()
        setTransactionObservers()
    }

    private fun setupViewClickListener() {
        binding.btnRetry.setOnClickListener {
                transactionAdapter.retry()
        }
    }


    private fun setupAdapter() {
        binding.recyclerviewTransaction.apply {
            layoutManager = recyclerLayoutManager
            adapter = transactionAdapter
            setHasFixedSize(false)
            addItemDecoration(
                DividerItemDecoration(context, LinearLayoutManager.VERTICAL)
            )
            addLoadStateListener()
        }
    }

    private fun addLoadStateListener() {
        transactionAdapter.addLoadStateListener { loadState ->
            binding.apply {
                progressCircular.isVisible = loadState.source.refresh is LoadState.Loading
                recyclerviewTransaction.isVisible = loadState.source.refresh is LoadState.NotLoading
                btnRetry.isVisible = loadState.source.refresh is LoadState.Error
                tvErrorMessage.isVisible = loadState.source.refresh is LoadState.Error
                tvNoData.isVisible = (
                        loadState.source.refresh is LoadState.NotLoading &&
                                transactionAdapter.itemCount < 1
                        )
            }
        }
    }

    private fun setTransactionObservers() {
        transactionViewModel.transactionLiveData.observe(viewLifecycleOwner) { transaction ->
            transactionAdapter.submitData(lifecycle, transaction)
        }
    }

    private fun getTransactions() {
        transactionViewModel.getTransaction()
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentTransactionBinding {
        return FragmentTransactionBinding.inflate(inflater, container, false)
    }
}
