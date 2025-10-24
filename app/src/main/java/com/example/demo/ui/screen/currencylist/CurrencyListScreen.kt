package com.example.demo.ui.screen.currencylist

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.demo.domain.model.CurrencyInfo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrencyListScreen(
    uiState: CurrencyListUIState,
    onAction: (CurrencyListAction) -> Unit,
    onNavigateAction: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {
        CurrencyListTopBar(
            searchQuery = uiState.searchQuery,
            onSearchQueryChange = { query ->
                onAction(CurrencyListAction.UpdateSearchQuery(query))
            },
            onClearSearch = {
                onAction(CurrencyListAction.ClearSearch)
            },
            onNavigateAction = onNavigateAction
        )
        
        if (uiState.filteredList.isEmpty()) {
            EmptyState(
                isSearching = uiState.isSearching,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            CurrencyList(
                currencies = uiState.filteredList,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrencyListTopBar(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onClearSearch: () -> Unit,
    onNavigateAction: (() -> Unit)?,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = {
            TextField(
                value = searchQuery,
                onValueChange = onSearchQueryChange,
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Search currencies...") },
                singleLine = true,
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = onClearSearch) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Clear search"
                            )
                        }
                    }
                },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.3f),
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.3f),
                    focusedIndicatorColor = MaterialTheme.colorScheme.surface.copy(alpha = 0f),
                    unfocusedIndicatorColor = MaterialTheme.colorScheme.surface.copy(alpha = 0f),
                )
            )
        },
        navigationIcon = {
            onNavigateAction?.let { navigateUp ->
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            }
        },
        modifier = modifier
    )
}


@Composable
fun CurrencyList(
    currencies: List<CurrencyInfo>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        items(currencies, key = { it.id }) { currency ->
            CurrencyCard(currency = currency)
            if (currency != currencies.last()) {
                HorizontalDivider(
                    modifier = Modifier.padding(start = 60.dp),
                    color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                )
            }
        }
    }
}

@Composable
fun CurrencyCard(
    currency: CurrencyInfo,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            modifier = Modifier.size(32.dp),
            shape = CircleShape,
            color = MaterialTheme.colorScheme.outlineVariant,
            tonalElevation = 2.dp
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Text(
                    text = currency.name.first().uppercase(),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = currency.name,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f)
        )

        Text(
            text = currency.code ?: currency.symbol,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(end = 8.dp)
        )

        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
        )
    }
}

@Composable
fun EmptyState(
    isSearching: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = "Empty state",
            modifier = Modifier.size(120.dp),
            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "No Results",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = if (isSearching) "Try adjusting your search" else "Load data from the buttons above",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
        )
    }
}

// ==================== Previews ====================

@Preview(name = "Currency Card", showBackground = true)
@Composable
fun PreviewCurrencyCard() {
    MaterialTheme {
        CurrencyCard(
            currency = CurrencyInfo("BTC", "Bitcoin", "BTC")
        )
    }
}

@Preview(name = "Currency List", showBackground = true, heightDp = 600)
@Composable
fun PreviewCurrencyList() {
    MaterialTheme {
        CurrencyList(
            currencies = listOf(
                CurrencyInfo("BTC", "Bitcoin", "BTC"),
                CurrencyInfo("ETH", "Ethereum", "ETH"),
                CurrencyInfo("XRP", "Ripple", "XRP"),
                CurrencyInfo("BCH", "Bitcoin Cash", "BCH"),
                CurrencyInfo("LTC", "Litecoin", "LTC")
            )
        )
    }
}

@Preview(name = "Empty State (No Data)", showBackground = true)
@Composable
fun PreviewEmptyStateNoData() {
    MaterialTheme {
        EmptyState(isSearching = false)
    }
}

@Preview(name = "Empty State (Search)", showBackground = true)
@Composable
fun PreviewEmptyStateSearch() {
    MaterialTheme {
        EmptyState(isSearching = true)
    }
}
