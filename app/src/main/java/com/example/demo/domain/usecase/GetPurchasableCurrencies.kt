package com.example.demo.domain.usecase

import com.example.demo.domain.model.CurrencyInfo
import com.example.demo.domain.repository.CurrencyRepository
import javax.inject.Inject

interface GetPurchasableCurrencies {
    suspend operator fun invoke(): List<CurrencyInfo>
}

class GetPurchasableCurrenciesImpl @Inject constructor(
    private val repository: CurrencyRepository
) : GetPurchasableCurrencies {
    override suspend operator fun invoke(): List<CurrencyInfo> {
        return repository.fetchPurchasableCurrenciesFromAPI()
    }
}
