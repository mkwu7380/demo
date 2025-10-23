package com.example.demo.data.repository

import com.example.demo.data.SampleData
import com.example.demo.data.local.CurrencyDao
import com.example.demo.data.mapper.toDomain
import com.example.demo.data.mapper.toEntity
import com.example.demo.domain.model.CurrencyInfo
import com.example.demo.domain.repository.CurrencyRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CurrencyRepositoryImpl @Inject constructor(
    private val currencyDao: CurrencyDao
) : CurrencyRepository {
    
    override fun getAllCurrenciesFromDatabase(): Flow<List<CurrencyInfo>> {
        return currencyDao.getAllCurrencies().map { entities ->
            entities.toDomain()
        }
    }
    
    override suspend fun insertCurrencies(currencies: List<CurrencyInfo>) {
        currencyDao.insertCurrencies(currencies.toEntity())
    }
    
    override suspend fun clearAllCurrencies() {
        currencyDao.clearAll()
    }
    
    override suspend fun fetchCryptoCurrenciesFromAPI(): List<CurrencyInfo> {
        delay(800)
        return SampleData.cryptoCurrencies
    }
    
    override suspend fun fetchFiatCurrenciesFromAPI(): List<CurrencyInfo> {
        delay(800)
        return SampleData.fiatCurrencies
    }
    
    override suspend fun fetchPurchasableCurrenciesFromAPI(): List<CurrencyInfo> {
        delay(1000)
        return SampleData.getPurchasableCurrencies()
    }
}
