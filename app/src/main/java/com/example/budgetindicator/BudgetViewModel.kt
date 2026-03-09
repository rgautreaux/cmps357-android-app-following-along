package com.example.budgetindicator

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

/**
 * Manages the budget calculation state.
 *
 * Exposes:
 *  - [balance]     the current bank balance entered by the user
 *  - [dailyBudget] the computed amount available per day for the rest of the month
 */
class BudgetViewModel(
    private val preferencesRepository: PreferencesRepository
) : ViewModel() {

    private val _balance = MutableStateFlow(0.0)
    val balance: StateFlow<Double> = _balance.asStateFlow()

    private val _dailyBudget = MutableStateFlow(0.0)
    val dailyBudget: StateFlow<Double> = _dailyBudget.asStateFlow()

    init {
        viewModelScope.launch {
            preferencesRepository.getBalance().collect { savedBalance ->
                _balance.value = savedBalance
                _dailyBudget.value = computeDailyBudget(savedBalance)
            }
        }
    }

    /** Persist a new balance and recompute the daily budget. */
    fun updateBalance(newBalance: Double) {
        _balance.value = newBalance
        _dailyBudget.value = computeDailyBudget(newBalance)
        viewModelScope.launch {
            preferencesRepository.saveBalance(newBalance)
        }
    }

    companion object {

        /**
         * Divides [balance] by the number of days remaining in the current month
         * (including today), with a floor of 1 day to avoid division by zero.
         */
        fun computeDailyBudget(balance: Double): Double {
            val today = LocalDate.now()
            val remainingDays = (today.lengthOfMonth() - today.dayOfMonth + 1).coerceAtLeast(1)
            return balance / remainingDays
        }

        fun factory(preferencesRepository: PreferencesRepository): ViewModelProvider.Factory =
            viewModelFactory {
                initializer { BudgetViewModel(preferencesRepository) }
            }
    }
}
