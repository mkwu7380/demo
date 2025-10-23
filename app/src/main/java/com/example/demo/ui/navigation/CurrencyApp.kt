package com.example.demo.ui.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.demo.domain.model.CurrencyInfo
import com.example.demo.ui.screen.demo.DemoScreen
import com.example.demo.ui.screen.demo.DemoViewModel
import com.example.demo.ui.screen.demo.DemoViewModelFactory
import com.example.demo.ui.screen.currencylist.CurrencyListScreen
import com.example.demo.ui.screen.currencylist.CurrencyListViewModel

object NavigationRoutes {
    const val DEMO = "demo"
    const val CURRENCY_LIST = "currency_list"
}

@Composable
fun CurrencyApp(
    demoViewModelFactory: DemoViewModelFactory
) {
    val navController = rememberNavController()
    
    MaterialTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            CurrencyNavHost(
                navController = navController,
                demoViewModelFactory = demoViewModelFactory
            )
        }
    }
}

@Composable
private fun CurrencyNavHost(
    navController: NavHostController,
    demoViewModelFactory: DemoViewModelFactory
) {
    var currentCurrencies by remember { mutableStateOf<List<CurrencyInfo>>(emptyList()) }
    
    NavHost(
        navController = navController,
        startDestination = NavigationRoutes.DEMO
    ) {
        demoScreen(
            navController = navController,
            demoViewModelFactory = demoViewModelFactory,
            onCurrenciesLoaded = { currencies ->
                currentCurrencies = currencies
            }
        )
        
        currencyListScreen(
            navController = navController,
            getCurrencies = { currentCurrencies }
        )
    }
}

private fun NavGraphBuilder.demoScreen(
    navController: NavHostController,
    demoViewModelFactory: DemoViewModelFactory,
    onCurrenciesLoaded: (List<CurrencyInfo>) -> Unit
) {
    composable(NavigationRoutes.DEMO) {
        val demoViewModel: DemoViewModel = viewModel(factory = demoViewModelFactory)
        
        val loadedCurrencies by demoViewModel.loadedCurrencies.collectAsState()
        
        LaunchedEffect(loadedCurrencies) {
            loadedCurrencies?.let { currencies ->
                onCurrenciesLoaded(currencies)
                navController.navigate(NavigationRoutes.CURRENCY_LIST) {
                    launchSingleTop = true
                }
                demoViewModel.clearLoadedCurrencies()
            }
        }
        
        DemoScreen(
            viewModel = demoViewModel,
            onLoadCryptoList = demoViewModel::loadCryptoCurrencies,
            onLoadFiatList = demoViewModel::loadFiatCurrencies,
            onLoadPurchasableList = demoViewModel::loadPurchasableCurrencies
        )
    }
}

private fun NavGraphBuilder.currencyListScreen(
    navController: NavHostController,
    getCurrencies: () -> List<CurrencyInfo>
) {
    composable(NavigationRoutes.CURRENCY_LIST) {
        val currencies = getCurrencies()
        val currencyListViewModel: CurrencyListViewModel = viewModel()
        
        CurrencyListScreen(
            currencies = currencies,
            viewModel = currencyListViewModel,
            onNavigateUp = navController::navigateUp
        )
    }
}
