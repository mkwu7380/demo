package com.example.demo.ui.screen.demo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.demo.domain.model.CurrencyInfo
import com.example.demo.domain.model.CurrencyType
import com.example.demo.domain.usecase.ClearDatabase
import com.example.demo.domain.usecase.GetCryptoCurrencies
import com.example.demo.domain.usecase.GetFiatCurrencies
import com.example.demo.domain.usecase.GetPurchasableCurrencies
import com.example.demo.domain.usecase.InsertCurrencies
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed class DemoAction {
    data object ClearDatabase : DemoAction()
    data object InsertData : DemoAction()
    data object LoadCryptoCurrencies : DemoAction()
    data object LoadFiatCurrencies : DemoAction()
    data object LoadPurchasableCurrencies : DemoAction()
    data object ClearLoadedCurrencies : DemoAction()
    data object ClearMessage : DemoAction()
}

data class DemoUIState(
    val message: String? = null,
    val isLoading: Boolean = false,
    val loadedCurrencies: List<CurrencyInfo>? = null,
    val currencyType: CurrencyType? = null
)

class DemoViewModel(
    private val clearDatabase: ClearDatabase,
    private val insertCurrencies: InsertCurrencies,
    private val getCryptoCurrencies: GetCryptoCurrencies,
    private val getFiatCurrencies: GetFiatCurrencies,
    private val getPurchasableCurrencies: GetPurchasableCurrencies
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(DemoUIState())
    val uiState: StateFlow<DemoUIState> = _uiState.asStateFlow()
    
    fun onAction(action: DemoAction) {
        when (action) {
            is DemoAction.ClearDatabase -> handleClearDatabase()
            is DemoAction.InsertData -> handleInsertData()
            is DemoAction.LoadCryptoCurrencies -> handleLoadCryptoCurrencies()
            is DemoAction.LoadFiatCurrencies -> handleLoadFiatCurrencies()
            is DemoAction.LoadPurchasableCurrencies -> handleLoadPurchasableCurrencies()
            is DemoAction.ClearLoadedCurrencies -> handleClearLoadedCurrencies()
            is DemoAction.ClearMessage -> handleClearMessage()
        }
    }
    
    private fun handleClearDatabase() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                clearDatabase.invoke()
                _uiState.update { it.copy(
                    message = "Database cleared successfully",
                    isLoading = false
                ) }
            } catch (e: Exception) {
                _uiState.update { it.copy(
                    message = "Error clearing database: ${e.message}",
                    isLoading = false
                ) }
            }
        }
    }
    
    private fun handleInsertData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                insertCurrencies()
                _uiState.update { it.copy(
                    message = "Currencies inserted into database successfully",
                    isLoading = false
                ) }
            } catch (e: Exception) {
                _uiState.update { it.copy(
                    message = "Error inserting data: ${e.message}",
                    isLoading = false
                ) }
            }
        }
    }
    
    private fun handleLoadCryptoCurrencies() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val cryptoCurrencies = getCryptoCurrencies()
                _uiState.update { it.copy(
                    loadedCurrencies = cryptoCurrencies,
                    currencyType = CurrencyType.CRYPTO,
                    message = "Loaded ${cryptoCurrencies.size} crypto currencies from database",
                    isLoading = false
                ) }
            } catch (e: Exception) {
                _uiState.update { it.copy(
                    message = "Error loading crypto currencies: ${e.message}",
                    isLoading = false
                ) }
            }
        }
    }
    
    private fun handleLoadFiatCurrencies() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val fiatCurrencies = getFiatCurrencies()
                _uiState.update { it.copy(
                    loadedCurrencies = fiatCurrencies,
                    currencyType = CurrencyType.FIAT,
                    message = "Loaded ${fiatCurrencies.size} fiat currencies from database",
                    isLoading = false
                ) }
            } catch (e: Exception) {
                _uiState.update { it.copy(
                    message = "Error loading fiat currencies: ${e.message}",
                    isLoading = false
                ) }
            }
        }
    }
    
    private fun handleLoadPurchasableCurrencies() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val currencies = getPurchasableCurrencies()
                _uiState.update { it.copy(
                    loadedCurrencies = currencies,
                    currencyType = CurrencyType.ALL,
                    message = "Loaded ${currencies.size} purchasable currencies from API",
                    isLoading = false
                ) }
            } catch (e: Exception) {
                _uiState.update { it.copy(
                    message = "Error loading purchasable currencies: ${e.message}",
                    isLoading = false
                ) }
            }
        }
    }
    
    private fun handleClearLoadedCurrencies() {
        _uiState.update { it.copy(loadedCurrencies = null, currencyType = null) }
    }
    
    private fun handleClearMessage() {
        _uiState.update { it.copy(message = null) }
    }
}
