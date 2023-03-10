package com.cbsindia.cbs.ui.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.cbsindia.cbs.util.Constant.KEY_DIALOG_DESCRIPTION
import com.cbsindia.cbs.util.Constant.KEY_DIALOG_TITLE

class DialogWithOkButton : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)
        val title = arguments?.getString(KEY_DIALOG_TITLE)
        val description = arguments?.getString(KEY_DIALOG_DESCRIPTION)

        return AlertDialog.Builder(context)
            .setTitle(title)
            .setMessage(description)
            .setPositiveButton(getString(android.R.string.ok)) { _, _ -> }
            .create()
    }
}
