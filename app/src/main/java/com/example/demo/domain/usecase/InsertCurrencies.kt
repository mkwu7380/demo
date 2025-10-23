package com.example.demo.domain.usecase

import com.example.demo.domain.repository.CurrencyRepository
import javax.inject.Inject

interface InsertCurrencies {
    suspend operator fun invoke()
}

class InsertCurrenciesImpl @Inject constructor(
    private val repository: CurrencyRepository
) : InsertCurrencies {
    override suspend operator fun invoke() {
        val cryptoCurrencies = repository.fetchCryptoCurrenciesFromAPI()
        val fiatCurrencies = repository.fetchFiatCurrenciesFromAPI()
        val allCurrencies = cryptoCurrencies + fiatCurrencies
        
        repository.insertCurrencies(allCurrencies)
    }
}
