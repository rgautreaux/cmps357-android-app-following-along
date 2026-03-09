package com.example.budgetindicator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.budgetindicator.ui.BudgetScreen
import com.example.budgetindicator.ui.theme.BudgetIndicatorTheme

/**
 * Single-activity host for the Budget Indicator app.
 *
 * Wires [PreferencesRepository] → [BudgetViewModel] → [BudgetScreen].
 */
class MainActivity : ComponentActivity() {

    private lateinit var preferencesRepository: PreferencesRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        preferencesRepository = PreferencesRepository(applicationContext)

        setContent {
            BudgetIndicatorTheme {
                val viewModel: BudgetViewModel = viewModel(
                    factory = BudgetViewModel.factory(preferencesRepository)
                )
                BudgetScreen(viewModel = viewModel)
            }
        }
    }
}
