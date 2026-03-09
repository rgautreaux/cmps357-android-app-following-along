package com.rgautreaux.budgetapp

import androidx.compose.ui.graphics.Color
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate

class ExampleUnitTest {

    @Test
    fun daysLeftInMonth_lastDay_returnsOne() {
        val lastDayOfJan = LocalDate.of(2024, 1, 31)
        assertEquals(1, daysLeftInMonth(lastDayOfJan))
    }

    @Test
    fun daysLeftInMonth_firstDay_returnsMonthLength() {
        val firstDayOfJan = LocalDate.of(2024, 1, 1)
        assertEquals(31, daysLeftInMonth(firstDayOfJan))
    }

    @Test
    fun perDay_dividesByDaysLeft() {
        assertEquals(20.0, perDay(400.0, 20), 0.001)
    }

    @Test
    fun perDay_zeroGuard() {
        assertEquals(0.0, perDay(400.0, 0), 0.001)
    }

    @Test
    fun colorForPerDay_low_isRed() {
        assertEquals(Color.Red, colorForPerDay(20.0))
    }

    @Test
    fun colorForPerDay_moderate_isAmber() {
        assertEquals(Color(0xFFFFC107), colorForPerDay(25.0))
    }

    @Test
    fun colorForPerDay_good_isLightGreen() {
        assertEquals(Color(0xFF8BC34A), colorForPerDay(35.0))
    }

    @Test
    fun colorForPerDay_high_isGreen() {
        assertEquals(Color(0xFF4CAF50), colorForPerDay(50.0))
    }
}
