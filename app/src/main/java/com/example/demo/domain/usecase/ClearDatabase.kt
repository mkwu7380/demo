package com.example.demo.domain.usecase

import com.example.demo.domain.repository.CurrencyRepository
import javax.inject.Inject

interface ClearDatabase {
    suspend operator fun invoke()
}

class ClearDatabaseImpl @Inject constructor(
    private val repository: CurrencyRepository
) : ClearDatabase {
    override suspend operator fun invoke() {
        repository.clearAllCurrencies()
    }
}
