package com.example.demo.domain.usecase

import com.example.demo.domain.model.CurrencyInfo
import com.example.demo.domain.model.CurrencyType
import com.example.demo.domain.repository.CurrencyRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface ObserveAllCurrencies {
    operator fun invoke(type: CurrencyType = CurrencyType.ALL): Flow<List<CurrencyInfo>>
}

class ObserveAllCurrenciesImpl @Inject constructor(
    private val repository: CurrencyRepository
) : ObserveAllCurrencies {
    override operator fun invoke(type: CurrencyType): Flow<List<CurrencyInfo>> {
        return repository.getAllCurrenciesFromDatabase().map { currencies ->
            when (type) {
                CurrencyType.ALL -> currencies
                CurrencyType.CRYPTO -> currencies.filter { it.code == null }
                CurrencyType.FIAT -> currencies.filter { it.code != null }
            }
        }
    }
}
