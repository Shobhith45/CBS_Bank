package com.cbsindia.cbs.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.cbsindia.cbs.R
import com.cbsindia.cbs.data.remote.Status
import com.cbsindia.cbs.data.repository.base.PrefRepository
import com.cbsindia.cbs.databinding.FragmentDepositBinding
import com.cbsindia.cbs.ui.viewmodels.DepositEvent
import com.cbsindia.cbs.ui.viewmodels.DepositViewModel
import com.cbsindia.cbs.ui.viewmodels.MainViewModel
import com.cbsindia.cbs.util.ApiErrorHelper.getErrorMessage
import com.cbsindia.cbs.util.DialogUtil
import com.cbsindia.cbs.util.TextUtil
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class DepositFragment : BaseFragment<FragmentDepositBinding>() {

    private val depositViewModel: DepositViewModel by viewModel()
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
    ): FragmentDepositBinding {
        return FragmentDepositBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        setupViewListeners()
        observeMainBalance()
        setDepositResponseObservers()
        setFormObserver()
    }

    private fun initViews() {
        binding.etDepositBalance.requestFocus()
    }

    private fun setupViewListeners() {
        binding.apply {
            fabDeposit.setOnClickListener {
                val amount = etDepositBalance.text.toString()
                val mPin = etMpin.text.toString()
                depositViewModel.validateForm(
                    amount,
                    mPin,
                    savedMPin
                )
            }
        }
    }

    private fun setFormObserver() {
        binding.apply {
            depositViewModel.formResponse.observe(viewLifecycleOwner) {
                it.getContentIfNotHandled()?.let { event ->
                    when (event) {
                        is DepositEvent.EmptyBalance -> {
                            etDepositBalance.error = getString(R.string.error_empty_amount)
                            etDepositBalance.requestFocus()
                        }
                        is DepositEvent.EmptyMpin -> {
                            etMpin.error = getString(R.string.err_empty_mpin)
                            etMpin.requestFocus()
                        }
                        is DepositEvent.LessMpinLength -> {
                            etMpin.error = getString(R.string.err_less_mpin_length)
                            etMpin.requestFocus()
                        }
                        is DepositEvent.LargeAmount -> {
                            etDepositBalance.error = getString(R.string.error_large_amount)
                            etDepositBalance.requestFocus()
                        }
                        is DepositEvent.MpinNotMatching -> {
                            etMpin.error = getString(R.string.mpin_doesnt_match_confirm_mpin)
                            etMpin.requestFocus()
                        }
                        is DepositEvent.ZeroAmount -> {
                            etDepositBalance.error = getString(R.string.error_zero_amount)
                            etDepositBalance.requestFocus()
                        }
                    }
                }
            }
        }
    }

    private fun setDepositResponseObservers() {
        depositViewModel.depositResponse.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { state ->
                when (state.status) {
                    Status.SUCCESS -> {
                        if (state.data != null) {
                            binding.apply {
                                mainViewModel.getBalance()
                                toggleProgressBar(false)
                                clearForm()
                                DialogUtil.showDialogWithOkButton(
                                    childFragmentManager,
                                    getString(R.string.info_dialog_title),
                                    getString(R.string.success_deposit)
                                )
                            }
                        }
                    }
                    Status.LOADING -> {
                        toggleProgressBar(true)
                    }
                    Status.ERROR -> {
                        toggleProgressBar(false)
                        DialogUtil.showDialogWithOkButton(
                            childFragmentManager,
                            getString(R.string.error_dialog_title),
                            getErrorMessage(state.message.toString())
                        )
                    }
                }
            }
        }
    }

    private fun clearForm() {
        binding.apply {
            etDepositBalance.text.clear()
            etMpin.text?.clear()
        }
    }

    private fun observeMainBalance() {
        mainViewModel.balanceLiveData.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    if (it.data != null) {
                        binding.apply {
                            tvAvailableBalance.text =
                                TextUtil.addCommaToBalance(it.data.balance)
                        }
                    }
                }
                Status.ERROR -> {
                    //No Operation
                }
                Status.LOADING -> {
                    //No Operation
                }
            }
        }
    }

    private fun toggleProgressBar(isVisible:Boolean){
        binding.progressCircular.isVisible = isVisible
    }
}
