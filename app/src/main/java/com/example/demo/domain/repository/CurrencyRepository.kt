package com.example.demo.domain.repository

import com.example.demo.domain.model.CurrencyInfo
import kotlinx.coroutines.flow.Flow

interface CurrencyRepository {
    
    fun getAllCurrenciesFromDatabase(): Flow<List<CurrencyInfo>>
    
    suspend fun insertCurrencies(currencies: List<CurrencyInfo>)
    
    suspend fun clearAllCurrencies()
    
    suspend fun fetchCryptoCurrenciesFromAPI(): List<CurrencyInfo>
    
    suspend fun fetchFiatCurrenciesFromAPI(): List<CurrencyInfo>
    
    suspend fun fetchPurchasableCurrenciesFromAPI(): List<CurrencyInfo>
}
