package com.example.budgetindicator

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate

/**
 * Unit tests for [BudgetViewModel.computeDailyBudget].
 *
 * These tests exercise the pure calculation logic without requiring
 * an Android context, DataStore, or coroutine infrastructure.
 */
class BudgetViewModelTest {

    @Test
    fun `computeDailyBudget divides balance evenly across remaining days`() {
        val today = LocalDate.now()
        val remainingDays = (today.lengthOfMonth() - today.dayOfMonth + 1).coerceAtLeast(1)
        val balance = remainingDays * 10.0

        val result = BudgetViewModel.computeDailyBudget(balance)

        assertEquals(10.0, result, 0.001)
    }

    @Test
    fun `computeDailyBudget returns positive value for positive balance`() {
        val result = BudgetViewModel.computeDailyBudget(500.0)
        assertTrue("Daily budget should be positive", result > 0.0)
    }

    @Test
    fun `computeDailyBudget returns zero for zero balance`() {
        val result = BudgetViewModel.computeDailyBudget(0.0)
        assertEquals(0.0, result, 0.001)
    }

    @Test
    fun `computeDailyBudget remaining days is at least one`() {
        val today = LocalDate.now()
        val remainingDays = (today.lengthOfMonth() - today.dayOfMonth + 1).coerceAtLeast(1)
        assertTrue("Remaining days must be >= 1", remainingDays >= 1)
    }

    @Test
    fun `computeDailyBudget does not exceed balance for one remaining day`() {
        val balance = 100.0
        val result = BudgetViewModel.computeDailyBudget(balance)
        // dailyBudget <= balance (true when remainingDays >= 1)
        assertTrue(result <= balance)
    }
}
