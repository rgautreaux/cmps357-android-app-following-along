package com.example.budgetindicator.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.budgetindicator.BudgetViewModel

/**
 * Color thresholds for the bottom spending-flexibility bar.
 *
 *  >= $50 / day  → green  (comfortable)
 *  >= $20 / day  → amber  (moderate)
 *  >= $5  / day  → orange (tight)
 *   < $5  / day  → red    (critical)
 */
private fun dailyBudgetColor(dailyBudget: Double): Color = when {
    dailyBudget >= 50.0 -> Color(0xFF4CAF50)
    dailyBudget >= 20.0 -> Color(0xFFFFC107)
    dailyBudget >= 5.0  -> Color(0xFFFF9800)
    else                -> Color(0xFFF44336)
}

/**
 * Main screen composable.
 *
 * Accepts a balance via a text field, shows the computed daily budget, and
 * renders a color-coded bottom bar reflecting spending flexibility.
 */
@Composable
fun BudgetScreen(
    viewModel: BudgetViewModel,
    modifier: Modifier = Modifier
) {
    val balance by viewModel.balance.collectAsStateWithLifecycle()
    val dailyBudget by viewModel.dailyBudget.collectAsStateWithLifecycle()

    var balanceInput by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    // Pre-fill with any previously persisted balance on first load.
    LaunchedEffect(balance) {
        if (balance > 0.0 && balanceInput.isEmpty()) {
            balanceInput = "%.2f".format(balance)
        }
    }

    val barColor = dailyBudgetColor(dailyBudget)

    Scaffold(
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .background(barColor),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (dailyBudget > 0.0) "\$%.2f / day".format(dailyBudget)
                           else "Enter your balance above",
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        },
        modifier = modifier
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Daily Budget",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "How much can you spend each day for the rest of the month?",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(32.dp))

            OutlinedTextField(
                value = balanceInput,
                onValueChange = { balanceInput = it },
                label = { Text("Current Bank Balance ($)") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Decimal,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        balanceInput.toDoubleOrNull()?.let { viewModel.updateBalance(it) }
                        focusManager.clearFocus()
                    }
                ),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    balanceInput.toDoubleOrNull()?.let { viewModel.updateBalance(it) }
                    focusManager.clearFocus()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Calculate")
            }

            if (dailyBudget > 0.0) {
                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = "\$%.2f".format(dailyBudget),
                    style = MaterialTheme.typography.displayMedium,
                    color = barColor
                )

                Text(
                    text = "per day remaining this month",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
