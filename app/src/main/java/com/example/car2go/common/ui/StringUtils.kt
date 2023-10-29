package com.example.car2go.common.ui

fun String.replaceMobilePhonePattern(): String {
    val regex = Regex("(\\w{3})(\\w{3})(\\w{4})").find(this)
    return regex?.let {
        "${it.groupValues[1]}-${it.groupValues[2]}-${it.groupValues[3]}"
    } ?: this
}