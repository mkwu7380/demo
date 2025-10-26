package com.example.demo.ui.screen.demo

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.demo.ui.viewmodel.demo.DemoAction
import com.example.demo.ui.viewmodel.demo.DemoUIState

@Composable
fun DemoScreen(
    uiState: DemoUIState,
    onAction: (DemoAction) -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.message) {
        uiState.message?.let {
            snackbarHostState.showSnackbar(it)
            onAction(DemoAction.ClearMessage)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 40.dp),
        ) {
            if (uiState.isLoading) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        LinearProgressIndicator(
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Processing...",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }

            Text(
                text = "Database Operations",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            OutlinedButton(
                onClick = { onAction(DemoAction.ClearDatabase) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                enabled = !uiState.isLoading
            ) {
                Text("Clear Database")
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = { onAction(DemoAction.InsertData) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                enabled = !uiState.isLoading
            ) {
                Text("Insert Data into Database")
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Currency Lists",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Button(
                onClick = { onAction(DemoAction.LoadCryptoCurrencies) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                enabled = !uiState.isLoading
            ) {
                Text("Load Currency List A (Crypto)")
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = { onAction(DemoAction.LoadFiatCurrencies) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                enabled = !uiState.isLoading
            ) {
                Text("Load Currency List B (Fiat)")
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = { onAction(DemoAction.LoadPurchasableCurrencies) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                enabled = !uiState.isLoading
            ) {
                Text("Display Purchasable Currencies")
            }
        }

        SnackbarHost(
            hostState = snackbarHostState, modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Preview(showBackground = true, name = "Default State")
@Composable
fun PreviewDemoScreenDefault() {
    MaterialTheme {
        DemoScreen(uiState = DemoUIState(
            message = null, isLoading = false, loadedCurrencies = null
        ), onAction = {})
    }
}

@Preview(showBackground = true, name = "Loading State")
@Composable
fun PreviewDemoScreenLoading() {
    MaterialTheme {
        DemoScreen(uiState = DemoUIState(
            message = null, isLoading = true, loadedCurrencies = null
        ), onAction = {})
    }
}
