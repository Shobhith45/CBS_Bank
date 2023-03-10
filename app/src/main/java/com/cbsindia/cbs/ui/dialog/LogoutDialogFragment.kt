package com.cbsindia.cbs.ui.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.cbsindia.cbs.R
import com.cbsindia.cbs.ui.viewmodels.MainViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class LogoutDialogFragment : DialogFragment() {

    private val viewModel: MainViewModel by sharedViewModel()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.all_text_logout))
            .setMessage(getString(R.string.message_logout_confirmation))
            .setPositiveButton(getString(R.string.all_text_logout)) { _, _ ->
                viewModel.logoutUser()
                findNavController().navigate(R.id.action_logoutDialogFragment_to_loginFragment)
            }
            .setNegativeButton(getString(android.R.string.cancel)) { _, _ -> }
            .create()
}
