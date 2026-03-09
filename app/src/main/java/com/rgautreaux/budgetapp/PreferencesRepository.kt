package com.rgautreaux.budgetapp

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "budget_prefs")

class PreferencesRepository(private val context: Context) {

    private val LAST_BALANCE = doublePreferencesKey("last_balance")

    suspend fun saveBalance(balance: Double) {
        context.dataStore.edit { preferences ->
            preferences[LAST_BALANCE] = balance
        }
    }

    suspend fun loadBalance(): Double? {
        return context.dataStore.data
            .map { preferences -> preferences[LAST_BALANCE] }
            .first()
    }
}
