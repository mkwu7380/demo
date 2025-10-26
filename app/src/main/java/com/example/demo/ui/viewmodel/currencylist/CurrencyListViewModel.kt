package com.example.demo.ui.viewmodel.currencylist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.demo.domain.model.CurrencyInfo
import com.example.demo.domain.model.CurrencyType
import com.example.demo.domain.usecase.ObserveAllCurrencies
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed class CurrencyListAction {
    data class UpdateSearchQuery(val query: String) : CurrencyListAction()
    data object ClearSearch : CurrencyListAction()
}

data class CurrencyListUIState(
    val currencyList: List<CurrencyInfo> = emptyList(),
    val filteredList: List<CurrencyInfo> = emptyList(),
    val searchQuery: String = "",
    val isSearching: Boolean = false
)

class CurrencyListViewModel(
    private val observeAllCurrencies: ObserveAllCurrencies,
    private val currencyType: CurrencyType
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(CurrencyListUIState())
    val uiState: StateFlow<CurrencyListUIState> = _uiState.asStateFlow()
    
    init {
        observeCurrenciesFromDatabase()
    }
    
    private fun observeCurrenciesFromDatabase() {
        viewModelScope.launch {
            observeAllCurrencies(currencyType).collect { currencies ->
                _uiState.update { currentState ->
                    val filteredList = if (currentState.searchQuery.isEmpty()) {
                        currencies
                    } else {
                        val searchTerm = currentState.searchQuery.trim().lowercase()
                        currencies.filter { currency ->
                            matchesSearchCriteria(currency, searchTerm)
                        }
                    }
                    currentState.copy(
                        currencyList = currencies,
                        filteredList = filteredList
                    )
                }
            }
        }
    }
    
    fun onAction(action: CurrencyListAction) {
        when (action) {
            is CurrencyListAction.UpdateSearchQuery -> handleUpdateSearchQuery(action.query)
            is CurrencyListAction.ClearSearch -> handleClearSearch()
        }
    }
    
    private fun handleUpdateSearchQuery(query: String) {
        viewModelScope.launch {
            val isSearching = query.isNotEmpty()
            val filteredList = if (query.isEmpty()) {
                _uiState.value.currencyList
            } else {
                val searchTerm = query.trim().lowercase()
                _uiState.value.currencyList.filter { currency ->
                    matchesSearchCriteria(currency, searchTerm)
                }
            }
            
            _uiState.update { it.copy(
                searchQuery = query,
                isSearching = isSearching,
                filteredList = filteredList
            ) }
        }
    }
    
    private fun handleClearSearch() {
        handleUpdateSearchQuery("")
    }
    
    private fun matchesSearchCriteria(currency: CurrencyInfo, searchTerm: String): Boolean {
        val nameLower = currency.name.lowercase()
        val symbolLower = currency.symbol.lowercase()
        val codeLower = currency.code?.lowercase()
        
        if (nameLower.startsWith(searchTerm)) {
            return true
        }
        
        if (nameLower.contains(" $searchTerm")) {
            return true
        }
        
        if (symbolLower.startsWith(searchTerm)) {
            return true
        }
        
        if (codeLower != null && codeLower.startsWith(searchTerm)) {
            return true
        }
        
        return false
    }
}
