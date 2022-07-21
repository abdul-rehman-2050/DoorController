package com.asta.door.controller.utils

import android.annotation.SuppressLint
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.abs

fun String.isDisplayNameValid(): Boolean {
    return this.isNotBlank() && this.length > 2
}

fun String.isEmailValid(): Boolean {
    return this.isNotBlank() && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

fun String.isPasswordValid(): Boolean {
    return this.isNotBlank() && this.length > 5
}

fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}

fun EditText.addFocusChangeListener(afterFocusChanged: (String) -> Unit) {
    this.setOnFocusChangeListener { _, hasFocus ->
        if (!hasFocus) {
            afterFocusChanged.invoke(this.text.toString())
        }
    }
}

fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)