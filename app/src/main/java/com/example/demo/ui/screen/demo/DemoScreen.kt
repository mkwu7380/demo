package com.example.demo.ui.screen.demo

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Composable
fun DemoScreen(
    viewModel: DemoViewModel,
    onLoadCryptoList: () -> Unit,
    onLoadFiatList: () -> Unit,
    onLoadPurchasableList: () -> Unit
) {
    val message by viewModel.message.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    
    LaunchedEffect(message) {
        message?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearMessage()
        }
    }
    
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp, vertical = 24.dp),
        ) {
            Text(
                text = "Currency Demo",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            
            Text(
                text = "API Simulation + Database Persistence",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 4.dp, bottom = 24.dp)
            )
            
            if (isLoading) {
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
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            OutlinedButton(
                onClick = { viewModel.clearDatabase() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                enabled = !isLoading
            ) {
                Text("Clear Database")
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Button(
                onClick = { viewModel.insertData() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                enabled = !isLoading
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
                onClick = onLoadCryptoList,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                enabled = !isLoading
            ) {
                Text("Load Currency List A (Crypto)")
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Button(
                onClick = onLoadFiatList,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                enabled = !isLoading
            ) {
                Text("Load Currency List B (Fiat)")
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Button(
                onClick = onLoadPurchasableList,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                enabled = !isLoading
            ) {
                Text("Display Purchasable Currencies")
            }
        }
    }
}

@Preview(showBackground = true, heightDp = 600)
@Composable
fun PreviewDemoScreen() {
    MaterialTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Currency Demo",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            OutlinedButton(
                onClick = {},
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Clear Database")
            }
            
            Button(
                onClick = {},
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Insert Data into Database")
            }
            
            Button(
                onClick = {},
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Load Currency List A (Crypto)")
            }
            
            Button(
                onClick = {},
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Load Currency List B (Fiat)")
            }
            
            Button(
                onClick = {},
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Display Purchasable Currencies")
            }
        }
    }
}
