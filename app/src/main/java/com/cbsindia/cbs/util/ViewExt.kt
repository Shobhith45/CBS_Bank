package com.cbsindia.cbs.util

import android.view.View
import android.widget.ProgressBar

object ViewExt {
    fun showProgressBar(view: ProgressBar) {
        view.visibility = View.VISIBLE
    }
    fun hideProgressBar(view: ProgressBar) {
        view.visibility = View.GONE
    }
}
