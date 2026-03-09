package com.example.budgetindicator

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "budget_prefs")

/**
 * Handles persistence of the user's bank balance using Jetpack DataStore.
 */
class PreferencesRepository(private val context: Context) {

    private val balanceKey = doublePreferencesKey("balance")

    fun getBalance(): Flow<Double> = context.dataStore.data.map { preferences ->
        preferences[balanceKey] ?: 0.0
    }

    suspend fun saveBalance(balance: Double) {
        context.dataStore.edit { preferences ->
            preferences[balanceKey] = balance
        }
    }
}
