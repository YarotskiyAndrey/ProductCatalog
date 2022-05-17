package com.task.productcatalog.util

import android.content.Context
import android.widget.Toast
import com.task.productcatalog.R
import java.io.IOException

object ErrorManager {
    fun Context.showError(exception: Exception) {
        val message = getMessage(exception)
        showMessage(message)
    }

    private fun Context.getMessage(exception: Exception) = when (exception) {
        is IOException -> getString(R.string.error_no_internet)
        else -> exception.message ?: getString(R.string.error_unknown_exception)
    }

    private fun Context.showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}