package com.example.demo.ui.screen.demo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.demo.domain.model.CurrencyInfo
import com.example.demo.domain.usecase.ClearDatabase
import com.example.demo.domain.usecase.GetCryptoCurrencies
import com.example.demo.domain.usecase.GetFiatCurrencies
import com.example.demo.domain.usecase.GetPurchasableCurrencies
import com.example.demo.domain.usecase.InsertCurrencies
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DemoViewModel(
    private val clearDatabase: ClearDatabase,
    private val insertCurrencies: InsertCurrencies,
    private val getCryptoCurrencies: GetCryptoCurrencies,
    private val getFiatCurrencies: GetFiatCurrencies,
    private val getPurchasableCurrencies: GetPurchasableCurrencies
) : ViewModel() {
    
    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _loadedCurrencies = MutableStateFlow<List<CurrencyInfo>?>(null)
    val loadedCurrencies: StateFlow<List<CurrencyInfo>?> = _loadedCurrencies.asStateFlow()
    
    fun clearDatabase() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                this@DemoViewModel.clearDatabase.invoke()
                _message.value = "Database cleared successfully"
            } catch (e: Exception) {
                _message.value = "Error clearing database: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun insertData() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                insertCurrencies()
                _message.value = "Currencies inserted into database successfully"
            } catch (e: Exception) {
                _message.value = "Error inserting data: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun loadCryptoCurrencies() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val cryptoCurrencies = getCryptoCurrencies()
                _loadedCurrencies.value = cryptoCurrencies
                _message.value = "Loaded ${cryptoCurrencies.size} crypto currencies from database"
            } catch (e: Exception) {
                _message.value = "Error loading crypto currencies: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun loadFiatCurrencies() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val fiatCurrencies = getFiatCurrencies()
                _loadedCurrencies.value = fiatCurrencies
                _message.value = "Loaded ${fiatCurrencies.size} fiat currencies from database"
            } catch (e: Exception) {
                _message.value = "Error loading fiat currencies: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun loadPurchasableCurrencies() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val currencies = getPurchasableCurrencies()
                _loadedCurrencies.value = currencies
                _message.value = "Loaded ${currencies.size} purchasable currencies from API"
            } catch (e: Exception) {
                _message.value = "Error loading purchasable currencies: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun clearLoadedCurrencies() {
        _loadedCurrencies.value = null
    }
    
    fun clearMessage() {
        _message.value = null
    }
}
