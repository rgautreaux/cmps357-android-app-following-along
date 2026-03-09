package com.rgautreaux.budgetapp

import androidx.compose.ui.graphics.Color
import java.time.LocalDate
import java.time.temporal.ChronoUnit

fun daysLeftInMonth(today: LocalDate): Int {
    val lastDay = today.withDayOfMonth(today.lengthOfMonth())
    return (ChronoUnit.DAYS.between(today, lastDay) + 1).toInt()
}

fun perDay(balance: Double, daysLeft: Int): Double {
    if (daysLeft == 0) return 0.0
    return balance / daysLeft
}

fun colorForPerDay(value: Double): Color {
    return when {
        value <= 20 -> Color.Red
        value <= 30 -> Color(0xFFFFC107) // Amber
        value <= 40 -> Color(0xFF8BC34A) // Light Green
        else -> Color(0xFF4CAF50) // Green
    }
}
