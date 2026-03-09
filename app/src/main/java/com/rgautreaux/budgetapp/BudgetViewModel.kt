package com.rgautreaux.budgetapp

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

class BudgetViewModel(
    private val repository: PreferencesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        val daysLeft = daysLeftInMonth(LocalDate.now())
        _uiState.value = _uiState.value.copy(daysLeft = daysLeft)
        loadLastBalance()
    }

    fun onBalanceChanged(text: String) {
        val balance = text.toDoubleOrNull()
        val daysLeft = daysLeftInMonth(LocalDate.now())
        if (balance != null) {
            val pd = perDay(balance, daysLeft)
            val color = colorForPerDay(pd)
            _uiState.value = UiState(
                balanceInput = text,
                balance = balance,
                daysLeft = daysLeft,
                perDay = pd,
                barColor = color,
                error = null
            )
            saveBalanceIfValid()
        } else {
            _uiState.value = UiState(
                balanceInput = text,
                balance = null,
                daysLeft = daysLeft,
                perDay = null,
                barColor = Color.Gray,
                error = if (text.isNotEmpty()) "Invalid input" else null
            )
        }
    }

    fun loadLastBalance() {
        viewModelScope.launch {
            val lastBalance = repository.loadBalance()
            if (lastBalance != null) {
                onBalanceChanged(lastBalance.toString())
            }
        }
    }

    fun saveBalanceIfValid() {
        val balance = _uiState.value.balance ?: return
        viewModelScope.launch {
            repository.saveBalance(balance)
        }
    }

    class Factory(private val repository: PreferencesRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return BudgetViewModel(repository) as T
        }
    }
}
