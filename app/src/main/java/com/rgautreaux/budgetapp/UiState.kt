package com.rgautreaux.budgetapp

import androidx.compose.ui.graphics.Color

data class UiState(
    val balanceInput: String = "",
    val balance: Double? = null,
    val daysLeft: Int = 0,
    val perDay: Double? = null,
    val barColor: Color = Color.Gray,
    val error: String? = null
)
