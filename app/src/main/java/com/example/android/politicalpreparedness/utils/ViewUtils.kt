package com.example.android.politicalpreparedness.utils

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.FragmentActivity
import com.google.android.material.snackbar.Snackbar

fun hideKeyboard(activity: FragmentActivity, view: View) {
    val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}

fun showSnackBar(message: Int, view: View) {
    Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show()
}