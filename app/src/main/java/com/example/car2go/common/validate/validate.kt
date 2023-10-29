package com.example.car2go.common.validate

import android.util.Patterns
import android.widget.EditText

fun EditText.isEmailValid(): Boolean {
    val value = this.text
    return Patterns.EMAIL_ADDRESS.matcher(value).matches() && value.isNotEmpty()
}

fun EditText.isPhoneValid(): Boolean {
    val value = this.text
    return value.length == 10
}