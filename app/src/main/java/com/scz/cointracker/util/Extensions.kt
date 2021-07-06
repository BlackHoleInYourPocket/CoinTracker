package com.scz.cointracker.util

fun String.castDouble() =
    if (this.contains(",")) this.replace(',', '.').toDouble() else this.toDouble()

fun Double.format(digits: Int) = "%.${digits}f".format(this)