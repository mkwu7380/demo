package com.example.demo.domain.usecase

import com.example.demo.domain.model.CurrencyInfo
import com.example.demo.domain.repository.CurrencyRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

interface GetFiatCurrencies {
    suspend operator fun invoke(): List<CurrencyInfo>
}

class GetFiatCurrenciesImpl @Inject constructor(
    private val repository: CurrencyRepository
) : GetFiatCurrencies {
    override suspend operator fun invoke(): List<CurrencyInfo> {
        val allCurrencies = repository.getAllCurrenciesFromDatabase().first()
        return allCurrencies.filter { it.code != null }
    }
}
