package com.cbsindia.cbs.ui.view

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cbsindia.cbs.R
import com.cbsindia.cbs.data.remote.Status.SUCCESS
import com.cbsindia.cbs.data.remote.Status.ERROR
import com.cbsindia.cbs.data.remote.Status.LOADING
import com.cbsindia.cbs.data.repository.base.PrefRepository
import com.cbsindia.cbs.databinding.FragmentMoneyTransferBinding
import com.cbsindia.cbs.ui.viewmodels.MainViewModel
import com.cbsindia.cbs.ui.viewmodels.MoneyTransferEvent.EmptyAccountNumber
import com.cbsindia.cbs.ui.viewmodels.MoneyTransferEvent.EmptyAmount
import com.cbsindia.cbs.ui.viewmodels.MoneyTransferEvent.EmptyMpin
import com.cbsindia.cbs.ui.viewmodels.MoneyTransferEvent.MpinNotMatching
import com.cbsindia.cbs.ui.viewmodels.MoneyTransferEvent.LessMpinLength
import com.cbsindia.cbs.ui.viewmodels.MoneyTransferEvent.ZeroAmount
import com.cbsindia.cbs.ui.viewmodels.MoneyTransferEvent.LargeAmount
import com.cbsindia.cbs.ui.viewmodels.MoneyTransferEvent.LowBalance
import com.cbsindia.cbs.ui.viewmodels.MoneyTransferEvent.Success
import com.cbsindia.cbs.ui.viewmodels.MoneyTransferViewModel
import com.cbsindia.cbs.util.ApiErrorHelper.getErrorMessage
import com.cbsindia.cbs.util.DialogUtil
import com.cbsindia.cbs.util.ViewExt.hideProgressBar
import com.cbsindia.cbs.util.ViewExt.showProgressBar
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MoneyTransferFragment : BaseFragment<FragmentMoneyTransferBinding>() {

    private val moneyTransferViewModel: MoneyTransferViewModel by viewModel()
    private val mainViewModel: MainViewModel by sharedViewModel()
    private val prefRepository: PrefRepository by inject()
    private val savedMPin = prefRepository.getUserMpin()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewListeners()
        setValidateFormObservers()
        setTransferResponseObservers()
    }

    private fun setupViewListeners() {
        addMoneyToSendTextChangeListener()
        addSendButtonClickListener()
    }

    private fun addSendButtonClickListener() {
        binding.apply {
            btnSend.setOnClickListener {
                val amount = etMoneyToSend.text.toString()
                val accountNumber = etAccountNumber.text.toString()
                val mPin = etMpin.text.toString()
                moneyTransferViewModel.validateForm(
                    amount,
                    accountNumber,
                    mPin,
                    savedMPin,
                    mainViewModel.balance
                )
            }
        }
    }

    private fun addMoneyToSendTextChangeListener() {
        binding.apply {
            etMoneyToSend.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(data: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if (data.toString().isEmpty())
                        tvAmountToSend.text = getString(R.string.all_text_zero)
                    else
                        tvAmountToSend.text = data.toString()
                }

                override fun afterTextChanged(amount: Editable?) {}
            })
        }
    }

    private fun setTransferResponseObservers() {
        moneyTransferViewModel.transferViewModel.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { state ->
                when (state.status) {
                    SUCCESS -> {
                        if (state.data != null) {
                            binding.apply {
                                hideProgressBar(progressCircular)
                                DialogUtil.showDialogWithOkButton(
                                    childFragmentManager,
                                    getString(R.string.info_dialog_title),
                                    getString(R.string.success_money_transfer)
                                )
                                clearForm()
                            }
                        }
                    }
                    LOADING -> {
                        showProgressBar(binding.progressCircular)
                    }
                    ERROR -> {
                        hideProgressBar(binding.progressCircular)
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

    private fun setValidateFormObservers() {
        moneyTransferViewModel.formLiveData.observe(viewLifecycleOwner) {
            binding.apply {
                it.getContentIfNotHandled()?.let { event ->
                    when (event) {
                        EmptyAmount -> {
                            etMoneyToSend.error = getString(R.string.error_empty_amount)
                            etMoneyToSend.requestFocus()
                        }
                        ZeroAmount -> {
                            etMoneyToSend.error = getString(R.string.error_zero_amount)
                            etMoneyToSend.requestFocus()
                        }
                        EmptyAccountNumber -> {
                            etAccountNumber.error = getString(R.string.error_empty_acc_num)
                            etAccountNumber.requestFocus()
                        }
                        LargeAmount -> {
                            etMoneyToSend.error = getString(R.string.error_large_amount)
                            etMoneyToSend.requestFocus()
                        }
                        EmptyMpin -> {
                            etMpin.error = getString(R.string.err_empty_mpin)
                            etMpin.requestFocus()
                        }
                        LessMpinLength -> {
                            etMpin.error = getString(R.string.err_less_mpin_length)
                            etMpin.requestFocus()
                        }
                        MpinNotMatching -> {
                            etMpin.error = getString(R.string.mpin_doesnt_match_confirm_mpin)
                            etMpin.requestFocus()
                        }
                        LowBalance -> {
                            etMoneyToSend.error = getString(R.string.error_insufficient_balance)
                            etMoneyToSend.requestFocus()
                        }
                        Success -> {
                            val amount = etMoneyToSend.text.toString().toDouble()
                            val accountNumber = etAccountNumber.text.toString().toLong()
                            val body = hashMapOf<String, Any>(
                                getString(R.string.key_to_account_number) to accountNumber,
                                getString(R.string.key_amount) to amount
                            )
                            moneyTransferViewModel.transferMoney(body)
                        }
                    }
                }
            }
        }
    }

    private fun clearForm() {
        binding.apply {
            etMoneyToSend.text.clear()
            etAccountNumber.text.clear()
            etMpin.text?.clear()
        }
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentMoneyTransferBinding {
        return FragmentMoneyTransferBinding.inflate(inflater, container, false)
    }
}
