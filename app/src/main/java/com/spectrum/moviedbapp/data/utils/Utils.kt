package com.spectrum.moviedbapp.data.utils

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

object Utils {

    private const val DATE_FORMAT = "dd-mm-YYYY"

    fun getDate(dateString: String?): String {
        val formatter: DateFormat = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())
        val date: Date = dateString?.let { formatter.parse(it) } as Date
        val sdfDate = formatter.format(date)
        return sdfDate ?: ""
    }

}