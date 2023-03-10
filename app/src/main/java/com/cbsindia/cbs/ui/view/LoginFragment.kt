package com.cbsindia.cbs.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.cbsindia.cbs.R
import com.cbsindia.cbs.data.remote.Status
import com.cbsindia.cbs.data.repository.base.PrefRepository
import com.cbsindia.cbs.databinding.FragmentLoginBinding
import com.cbsindia.cbs.ui.viewmodels.LoginEvent
import com.cbsindia.cbs.ui.viewmodels.LoginViewModel
import com.cbsindia.cbs.util.ApiErrorHelper.getErrorMessage
import com.cbsindia.cbs.util.DialogUtil
import com.cbsindia.cbs.util.ViewExt.hideProgressBar
import com.cbsindia.cbs.util.ViewExt.showProgressBar
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginFragment : BaseFragment<FragmentLoginBinding>() {
    private val loginViewModel: LoginViewModel by viewModel()
    private val prefRepository: PrefRepository by inject()
    private val savedMPin = prefRepository.getUserMpin()
    private val isUserLoggedIn = prefRepository.getIsUserLoggedIn()
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
        loginViewModel.resetToken()
        checkUserLoggedIn()
        setupViewListeners()
        setValidateFormObservers()
        setLoginResponseObservers()
    }
    private fun checkUserLoggedIn() {
        if (loginViewModel.checkIfAlreadyLoggedIn()) {
            hideViewsIfLoggedIn()
        }
    }
    private fun hideViewsIfLoggedIn() {
        binding.apply {
            etCustomerId.visibility = View.GONE
            textView2.visibility = View.GONE
            tvCreateAccount.visibility = View.GONE
            tvCustomerIdHeading.visibility = View.GONE
        }
    }
    private fun setupViewListeners() {
        addLoginButtonClickListener()
        addCreateAccountButtonClickListener()
    }

    private fun addCreateAccountButtonClickListener() {
        binding.tvCreateAccount.setOnClickListener {
            findNavController().navigate(R.id.action_introFragment_to_createAccountFragment)
        }
    }
    private fun addLoginButtonClickListener() {
        binding.apply {
            btLogin.setOnClickListener {
                if (loginViewModel.checkIfAlreadyLoggedIn()) {
                    etCustomerId.setText(loginViewModel.getCustomerId())
                }
                val customerId = etCustomerId.text.toString()
                val mpin = etMpin.text.toString()
                loginViewModel.validateForm(
                    customerId,
                    mpin,
                    savedMPin.toString(),
                    isUserLoggedIn
                )
            }
        }
    }
    private fun setValidateFormObservers() {
        loginViewModel.formLiveData.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { event ->
                when (event) {
                    LoginEvent.EmptyCustomerId -> {
                        binding.apply {
                            etCustomerId.error = getString(R.string.err_empty_custid)
                            etCustomerId.requestFocus()
                        }
                    }
                    LoginEvent.EmptyMpin -> {
                        binding.apply {
                            etMpin.error = getString(R.string.err_empty_mpin)
                            etMpin.requestFocus()
                        }
                    }
                    LoginEvent.LessMpinLength -> {
                        binding.apply {
                            etMpin.error = getString(R.string.err_less_mpin_length)
                            etMpin.requestFocus()
                        }
                    }
                    LoginEvent.ValidMPin -> {
                        binding.apply {
                            etMpin.error = getString(R.string.mpin_doesnt_match_confirm_mpin)
                            etMpin.requestFocus()
                        }
                    }
                    LoginEvent.Success -> {
                        val customerId: String = if (loginViewModel.checkIfAlreadyLoggedIn()) {
                            loginViewModel.getCustomerId()
                        } else {
                            binding.etCustomerId.text.toString()
                        }
                        val mPin = binding.etMpin.text.toString()
                        loginViewModel.login(customerId, mPin)
                    }
                }
            }
        }
    }
    private fun setLoginResponseObservers() {
        loginViewModel.loginViewModel.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { state ->
                when (state.status) {
                    Status.SUCCESS -> {
                        if (state.data != null) {
                            binding.apply {
                                val customerId = etCustomerId.text.toString()
                                val token = state.data.token
                                val mPin = etMpin.text.toString().toLong()
                                hideProgressBar(progressCircular)
                                loginViewModel.saveLoginInfo(token, customerId, true, mPin)
                                findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
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
    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentLoginBinding {
        return FragmentLoginBinding.inflate(inflater, container, false)
    }
}