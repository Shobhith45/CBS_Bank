package com.cbsindia.cbs.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cbsindia.cbs.R
import com.cbsindia.cbs.data.remote.Status
import com.cbsindia.cbs.data.repository.base.PrefRepository
import com.cbsindia.cbs.databinding.FragmentWithdrawBinding
import com.cbsindia.cbs.ui.viewmodels.MainViewModel
import com.cbsindia.cbs.ui.viewmodels.WithdrawEvent
import com.cbsindia.cbs.ui.viewmodels.WithdrawViewModel
import com.cbsindia.cbs.util.ApiErrorHelper.getErrorMessage
import com.cbsindia.cbs.util.DialogUtil
import com.cbsindia.cbs.util.TextUtil
import com.cbsindia.cbs.util.ViewExt.hideProgressBar
import com.cbsindia.cbs.util.ViewExt.showProgressBar
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class WithdrawFragment : BaseFragment<FragmentWithdrawBinding>() {

    private val withdrawViewModel: WithdrawViewModel by viewModel()
    private val mainViewModel: MainViewModel by sharedViewModel()
    private val prefRepository: PrefRepository by inject()
    private val savedMPin = prefRepository.getUserMpin()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        return binding.root
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentWithdrawBinding {
        return FragmentWithdrawBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        setupViewListeners()
        observeMainBalance()
        setWithdrawResponseObservers()
        setFormObserver()
    }

    private fun initViews() {
        binding.apply {
            etWithdrawBalance.requestFocus()
        }
    }

    private fun setFormObserver() {
        binding.apply {
            withdrawViewModel.formResponse.observe(viewLifecycleOwner) {
                it.getContentIfNotHandled()?.let { event ->
                    when (event) {
                        is WithdrawEvent.EmptyBalance -> {
                            etWithdrawBalance.error = getString(R.string.error_empty_amount)
                            etWithdrawBalance.requestFocus()
                        }
                        is WithdrawEvent.EmptyMpin -> {
                            etMpin.error = getString(R.string.err_empty_mpin)
                            etMpin.requestFocus()
                        }
                        is WithdrawEvent.LessMpinLength -> {
                            etMpin.error = getString(R.string.err_less_mpin_length)
                            etMpin.requestFocus()
                        }
                        is WithdrawEvent.LargeAmount -> {
                            etWithdrawBalance.error = getString(R.string.error_large_amount)
                            etWithdrawBalance.requestFocus()
                        }
                        is WithdrawEvent.LowBalance -> {
                            etWithdrawBalance.error = getString(R.string.error_insufficient_balance)
                            etWithdrawBalance.requestFocus()
                        }
                        is WithdrawEvent.MpinNotMatching -> {
                            etMpin.error = getString(R.string.mpin_doesnt_match_confirm_mpin)
                            etMpin.requestFocus()
                        }
                        is WithdrawEvent.Success -> {
                            val amount = etWithdrawBalance.text.toString().toDouble()
                            withdrawViewModel.withdraw(amount)
                        }
                        is WithdrawEvent.ZeroAmount -> {
                            etWithdrawBalance.error = getString(R.string.error_zero_amount)
                            etWithdrawBalance.requestFocus()
                        }
                    }
                }
            }
        }
    }

    private fun setupViewListeners() {
        addSendButtonClickListener()
    }

    private fun setWithdrawResponseObservers() {
        withdrawViewModel.withdrawAmtViewModel.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { response ->
                when (response.status) {
                    Status.SUCCESS -> {
                        if (response.data != null) {
                            binding.apply {
                                mainViewModel.getBalance()
                                hideProgressBar(progressCircular)
                                clearForm()
                                DialogUtil.showDialogWithOkButton(
                                    childFragmentManager,
                                    getString(R.string.info_dialog_title),
                                    getString(R.string.success_withdraw)
                                )
                            }
                        }
                    }
                    Status.LOADING -> {
                        showProgressBar(binding.progressCircular)
                    }
                    Status.ERROR -> {
                        hideProgressBar(binding.progressCircular)
                        DialogUtil.showDialogWithOkButton(
                            childFragmentManager,
                            getString(R.string.error_dialog_title),
                            getErrorMessage(response.message.toString())
                        )
                    }
                }
            }
        }
    }

    private fun clearForm() {
        binding.apply {
            etWithdrawBalance.text.clear()
            etMpin.text?.clear()
        }
    }

    private fun addSendButtonClickListener() {
        binding.apply {
            fabWithdraw.setOnClickListener {
                val amount = etWithdrawBalance.text.toString()
                val mPin = etMpin.text.toString()
                withdrawViewModel.validateForm(
                    amount,
                    mPin,
                    savedMPin,
                    mainViewModel.balance
                )
            }
        }
    }

    private fun observeMainBalance() {
        mainViewModel.balanceLiveData.observe(viewLifecycleOwner) { response ->
            when (response.status) {
                Status.SUCCESS -> {
                    if (response.data != null) {
                        binding.apply {
                            tvAvailableBalance.text =
                                TextUtil.addCommaToBalance(response.data.balance)
                        }
                    }
                }
            }
        }
    }
}
