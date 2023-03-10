package com.cbsindia.cbs.util

import android.os.Bundle
import androidx.fragment.app.FragmentManager
import com.cbsindia.cbs.ui.dialog.DialogWithOkButton
import com.cbsindia.cbs.util.Constant.KEY_DIALOG_DESCRIPTION
import com.cbsindia.cbs.util.Constant.KEY_DIALOG_TITLE

object DialogUtil {
    fun showDialogWithOkButton(
        fragmentManager: FragmentManager,
        title: String,
        description: String
    ) {
        val dialog = DialogWithOkButton()
        val bundle = Bundle()
        bundle.putString(KEY_DIALOG_TITLE, title)
        bundle.putString(KEY_DIALOG_DESCRIPTION, description)
        dialog.arguments = bundle
        dialog.show(fragmentManager, null)
    }
}
