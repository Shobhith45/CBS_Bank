package com.cbsindia.cbs.ui.view

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.cbsindia.cbs.R
import com.cbsindia.cbs.data.remote.Status
import com.cbsindia.cbs.databinding.FragmentCreateAccountBinding
import com.cbsindia.cbs.ui.viewmodels.CreateAccountEvent
import com.cbsindia.cbs.ui.viewmodels.CreateAccountViewModel
import com.cbsindia.cbs.util.ApiErrorHelper.getErrorMessage
import com.cbsindia.cbs.util.DialogUtil
import com.cbsindia.cbs.util.ViewExt.hideProgressBar
import com.cbsindia.cbs.util.ViewExt.showProgressBar
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class CreateAccountFragment : BaseFragment<FragmentCreateAccountBinding>() {
    private val createAccountViewModel: CreateAccountViewModel by viewModel()
    private val calendar: Calendar = Calendar.getInstance()
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
        initViews()
        setupViewListeners()
        setValidateFormObservers()
        setCreateAccountResponseObservers()
    }
    private fun initViews() {
        binding.apply {
            etBirthdate.isFocusable = false
            etBirthdate.showSoftInputOnFocus = false
        }
    }
    private fun setupViewListeners() {
        addCreateAccountButtonClickListener()
        openDatePickerDialogListener()
    }
    private fun openDatePickerDialogListener() {
        val date =
            DatePickerDialog.OnDateSetListener { _, year, month, day ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, day)
                updateBirthdate()
            }
        binding.apply {
            etBirthdate.setOnClickListener {
                DatePickerDialog(
                    requireContext(),
                    date,
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
                ).show()
            }
        }
    }
    private fun updateBirthdate() {
        val myFormat = "dd/MM/yyyy"
        val dateFormat = SimpleDateFormat(myFormat, Locale.ROOT)
        binding.etBirthdate.setText(dateFormat.format(calendar.time))
    }
    private fun addCreateAccountButtonClickListener() {
        binding.apply {
            btCreateAccount.setOnClickListener {
                val fullName = etFullName.text.toString()
                val mobileNumber = etMobileNumber.text.toString()
                val uIDNumber = etAadharNumber.text.toString()
                val address = etAddress.text.toString()
                val birthDate = etBirthdate.text.toString()
                val parentsName = etParentName.text.toString()
                val nomineeName = etNomineeName.text.toString()
                val mpin = etMpin.text.toString()
                val confirmmpin = etConfirmMpin.text.toString()
                createAccountViewModel.validateForm(
                    fullName,
                    mobileNumber,
                    uIDNumber,
                    birthDate,
                    parentsName,
                    nomineeName,
                    mpin,
                    confirmmpin,
                    address
                )
            }
        }
    }
    private fun setValidateFormObservers() {
        createAccountViewModel.formLiveData.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { event ->
                when (event) {
                    CreateAccountEvent.EmptyFullName -> {
                        binding.apply {
                            etFullName.error = getString(R.string.err_empty_full_name)
                            etFullName.requestFocus()
                        }
                    }
                    CreateAccountEvent.EmptyPhoneNumber -> {
                        binding.apply {
                            etMobileNumber.error = getString(R.string.err_empty_mobile_number)
                            etMobileNumber.requestFocus()
                        }
                    }
                    CreateAccountEvent.EmptyUIDNumber -> {
                        binding.apply {
                            etAadharNumber.error = getString(R.string.err_empty_UID)
                            etAadharNumber.requestFocus()
                        }
                    }
                    CreateAccountEvent.EmptyBirthDate -> {
                        binding.apply {
                            etBirthdate.error = getString(R.string.err_empty_birth_date)
                            etBirthdate.requestFocus()
                        }
                    }
                    CreateAccountEvent.EmptyAddress -> {
                        binding.apply {
                            etAddress.error = getString(R.string.err_empty_address)
                            etAddress.requestFocus()
                        }
                    }
                    CreateAccountEvent.EmptyParentName -> {
                        binding.apply {
                            etParentName.error = getString(R.string.err_empty_parent_name)
                            etParentName.requestFocus()
                        }
                    }
                    CreateAccountEvent.EmptyNomineeName -> {
                        binding.apply {
                            etNomineeName.error = getString(R.string.err_empty_nominee_name)
                            etNomineeName.requestFocus()
                        }
                    }
                    CreateAccountEvent.EmptyMPIN -> {
                        binding.apply {
                            etMpin.error = getString(R.string.err_empty_mpin)
                            etMpin.requestFocus()
                        }
                    }
                    CreateAccountEvent.EmptyConfirmMPIN -> {
                        binding.apply {
                            etConfirmMpin.error = getString(R.string.err_empty_confirm_mpin)
                            etConfirmMpin.requestFocus()
                        }
                    }
                    CreateAccountEvent.LimitMobileNumber -> {
                        binding.apply {
                            etMobileNumber.error = getString(R.string.enter_10_digit_mobile_number)
                            etMobileNumber.requestFocus()
                        }
                    }
                    CreateAccountEvent.LimitAadhaarNumber -> {
                        binding.apply {
                            etAadharNumber.error =
                                getString(R.string.enter_12_digit_aadhaar_number)
                            etAadharNumber.requestFocus()
                        }
                    }
                    CreateAccountEvent.LimitMPIN -> {
                        binding.apply {
                            etMpin.error = getString(R.string.enter_6_digit_mpin)
                            etMpin.requestFocus()
                        }
                    }
                    CreateAccountEvent.MpinNotEqualsConfirmMpin -> {
                        binding.apply {
                            etConfirmMpin.error =
                                getString(R.string.mpin_doesnt_match_confirm_mpin)
                            etConfirmMpin.requestFocus()
                        }
                    }
                    CreateAccountEvent.Success -> {
                        val fullName = binding.etFullName.text.toString()
                        val mobileNumber = binding.etMobileNumber.text.toString()
                        val uIDNumber =
                            addFourDigitSpaceToAadhaarNumber(binding.etAadharNumber.text.toString())
                        val address = binding.etAddress.text.toString()
                        val birthDate = binding.etBirthdate.text.toString().replace("/", "")
                        val parentsName = binding.etParentName.text.toString()
                        val nomineeName = binding.etNomineeName.text.toString()
                        val mpin = binding.etMpin.text.toString()
                        val body = hashMapOf(
                            getString(R.string.key_full_name) to fullName,
                            getString(R.string.key_birth_date) to birthDate,
                            getString(R.string.key_address) to address,
                            getString(R.string.key_mobile_number) to mobileNumber,
                            getString(R.string.key_mpin) to mpin,
                            getString(R.string.key_parent_name) to parentsName,
                            getString(R.string.key_nominee_name) to nomineeName,
                            getString(R.string.key_UID) to uIDNumber
                        )
                        createAccountViewModel.createAccount(body)
                    }
                }
            }
        }
    }
    private fun setCreateAccountResponseObservers() {
        createAccountViewModel.createAccountViewModel.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { state ->
                when (state.status) {
                    Status.SUCCESS -> {
                        if (state.data != null) {
                            binding.apply {
                                hideProgressBar(progressCircular)
                                clearForm()
                                DialogUtil.showDialogWithOkButton(
                                    parentFragmentManager,
                                    getString(R.string.info_dialog_title),
                                    getString(
                                        R.string.message_register_success,
                                        state.data.customerDetails.customerNo
                                    )
                                )
                                findNavController().navigate(R.id.action_createAccountFragment_to_loginFragment)
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
                            getErrorMessage(state.message.toString())
                        )
                    }
                }
            }
        }
    }
    private fun clearForm() {
        binding.apply {
            etFullName.text?.clear()
            etMobileNumber.text?.clear()
            etBirthdate.text?.clear()
            etConfirmMpin.text?.clear()
            etMpin.text?.clear()
            etNomineeName.text?.clear()
            etParentName.text?.clear()
            etAddress.text?.clear()
            etAadharNumber.text?.clear()
        }
    }
    private fun addFourDigitSpaceToAadhaarNumber(number: String): String {
        return number.replace("....".toRegex(), "$0 ").trim()
    }
    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentCreateAccountBinding {
        return FragmentCreateAccountBinding.inflate(inflater,container,false)
    }
}