package com.example.car2go.common.ui

import android.app.DatePickerDialog
import android.content.Context
import com.example.car2go.R
import java.util.*

class DatePickerUtils(context: Context, isSpinnerType: Boolean = false) {
    private var dialog: DatePickerDialog
    private var callback: Callback? = null
    private val listener =
        DatePickerDialog.OnDateSetListener { datePicker, year, monthOfYear, dayOfMonth ->
            val dayStr = if (dayOfMonth < 10) "0${dayOfMonth}" else "$dayOfMonth"
            val mon = monthOfYear + 1
            val monthStr = if (mon < 10) "0${mon}" else "$mon"
            val dateSelectedFormat = "${dayStr}-${monthStr}-${year}"
            callback?.onDateSelected(dayOfMonth, monthOfYear, year, dateSelectedFormat)
        }

    init {
        val style = if (isSpinnerType) R.style.SpinnerDatePickerDialog else 0
        val cal = Calendar.getInstance()
        dialog = DatePickerDialog(
            context, style, listener,
            cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)
        )
    }

    fun showDialog(callback: Callback?) {
        val cal = Calendar.getInstance()
        val dayOfMonth = cal.get(Calendar.DAY_OF_MONTH)
        val month = cal.get(Calendar.MONTH)
        val year = cal.get(Calendar.YEAR)
        this.callback = callback
        dialog.datePicker.init(year, month, dayOfMonth, null)
        dialog.show()
    }

    fun setMinDate(minDate: Long) {
        dialog.datePicker.minDate = minDate
    }

    fun setMaxDate(maxDate: Long) {
        dialog.datePicker.maxDate = maxDate
    }

    interface Callback {
        fun onDateSelected(dayOfMonth: Int, month: Int, year: Int, dateSelectedFormat: String)
    }
}