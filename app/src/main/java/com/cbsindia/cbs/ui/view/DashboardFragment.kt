package com.cbsindia.cbs.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.cbsindia.cbs.R
import com.cbsindia.cbs.data.remote.Status.ERROR
import com.cbsindia.cbs.data.remote.Status.LOADING
import com.cbsindia.cbs.data.remote.Status.SUCCESS
import com.cbsindia.cbs.databinding.FragmentDashboardBinding
import com.cbsindia.cbs.ui.viewmodels.MainViewModel
import com.cbsindia.cbs.util.ApiErrorHelper.getErrorMessage
import com.cbsindia.cbs.util.TextUtil
import com.cbsindia.cbs.util.ViewExt.hideProgressBar
import com.cbsindia.cbs.util.ViewExt.showProgressBar
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class DashboardFragment : BaseFragment<FragmentDashboardBinding>() {

    private val mainViewModel: MainViewModel by sharedViewModel()
    private lateinit var navController: NavController

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
        navController = findNavController()
        setViewClickListeners()
        mainViewModel.getBalance()
        mainViewModel.getUserAccount()
        observeMainBalance()
        observeUserDetails()
    }

    private fun setViewClickListeners() {
        binding.apply {
            btnDeposit.setOnClickListener {
                navController.navigate(
                    R.id.action_homeFragment_to_depositFragment
                )
            }
            btnWithdraw.setOnClickListener {
                navController.navigate(
                    R.id.action_homeFragment_to_withdrawFragment
                )
            }
            btnMoneyTransfer.setOnClickListener {
                navController.navigate(
                    R.id.action_homeFragment_to_moneyTransferFragment
                )
            }
            btnTransaction.setOnClickListener {
                navController.navigate(
                    R.id.action_homeFragment_to_transactionFragment
                )
            }
        }
    }

    private fun observeUserDetails() {
        mainViewModel.userLiveData.observe(viewLifecycleOwner) { state ->
            when (state.status) {
                SUCCESS -> {
                    if (state.data != null) {
                        binding.apply {
                            tvAccountNumber.text = state.data.accountNum.toString()
                            tvUserName.text = state.data.name
                        }
                    }
                }
            }
        }
    }

    private fun observeMainBalance() {
        mainViewModel.balanceLiveData.observe(viewLifecycleOwner) { state ->
            when (state.status) {
                SUCCESS -> {
                    if (state.data != null) {
                        binding.apply {
                            hideProgressBar(progressCircular)
                            svContentParent.visibility = View.VISIBLE
                            tvMainBalance.text = TextUtil.addCommaToBalance(state.data.balance)
                        }
                    }
                }
                LOADING -> {
                    showProgressBar(binding.progressCircular)
                }
                ERROR -> {
                    hideProgressBar(binding.progressCircular)
                    Snackbar.make(
                        binding.root,
                        getErrorMessage(state.message.toString()),
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentDashboardBinding =
        FragmentDashboardBinding.inflate(inflater, container, false)
}
