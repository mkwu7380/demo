package com.example.demo.ui.screen.currencylist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.demo.domain.model.CurrencyInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CurrencyListViewModel : ViewModel() {
    
    private val _currencyList = MutableStateFlow<List<CurrencyInfo>>(emptyList())
    val currencyList: StateFlow<List<CurrencyInfo>> = _currencyList.asStateFlow()
    
    private val _filteredList = MutableStateFlow<List<CurrencyInfo>>(emptyList())
    val filteredList: StateFlow<List<CurrencyInfo>> = _filteredList.asStateFlow()
    
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()
    
    private val _isSearching = MutableStateFlow(false)
    val isSearching: StateFlow<Boolean> = _isSearching.asStateFlow()
    
    fun setCurrencyList(currencies: ArrayList<CurrencyInfo>) {
        viewModelScope.launch {
            _currencyList.value = currencies
            _filteredList.value = currencies
        }
    }
    
    fun updateSearchQuery(query: String) {
        viewModelScope.launch {
            _searchQuery.value = query
            _isSearching.value = query.isNotEmpty()
            
            if (query.isEmpty()) {
                _filteredList.value = _currencyList.value
            } else {
                val searchTerm = query.trim().lowercase()
                _filteredList.value = _currencyList.value.filter { currency ->
                    matchesSearchCriteria(currency, searchTerm)
                }
            }
        }
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
    
    fun clearSearch() {
        updateSearchQuery("")
    }
}
