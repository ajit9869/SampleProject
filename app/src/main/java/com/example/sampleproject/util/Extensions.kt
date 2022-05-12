package com.example.sampleproject.util

import android.graphics.Color
import android.view.View
import com.example.sampleproject.R
import com.google.android.material.snackbar.Snackbar


fun View.onSnackbar(msg: String) {
    //Snackbar(view)
    val snackbar = Snackbar.make(
        this, msg,
        Snackbar.LENGTH_LONG
    )

    snackbar.setAction(R.string.ok) { snackbar.dismiss() }
    val snackbarView = snackbar.view
    snackbarView.setBackgroundColor(Color.parseColor("#000000"))
    snackbar.setActionTextColor(Color.parseColor("#000000"))
    snackbar.show()
}

