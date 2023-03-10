package com.cbsindia.cbs.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.cbsindia.cbs.R
import com.cbsindia.cbs.databinding.FragmentWelcomeBinding
import com.cbsindia.cbs.ui.viewmodels.WelcomeViewModel
import org.koin.android.ext.android.inject

class WelcomeFragment : BaseFragment<FragmentWelcomeBinding>() {
    private val introViewModel: WelcomeViewModel by inject()
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
        setupViewListeners()
    }
    override fun onResume() {
        super.onResume()
        checkIntroScreenShown()
    }
    private fun checkIntroScreenShown() {
        if (introViewModel.isIntroScreenShown())
            startLoginScreen()
        else
            introViewModel.setIsIntoScreenShown(true)
    }
    private fun startLoginScreen() {
        findNavController().navigate(R.id.action_introFragment_to_loginFragment)
    }
    private fun setupViewListeners() {
        binding.btLogin.setOnClickListener {
            startLoginScreen()
        }
        binding.btCreateAccount.setOnClickListener {
            findNavController().navigate(R.id.action_introFragment_to_createAccountFragment)
        }
    }
    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentWelcomeBinding {
        return FragmentWelcomeBinding.inflate(inflater, container, false)
    }
}