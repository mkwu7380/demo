package com.example.demo.ui.screen.demo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.demo.domain.usecase.ClearDatabase
import com.example.demo.domain.usecase.GetCryptoCurrencies
import com.example.demo.domain.usecase.GetFiatCurrencies
import com.example.demo.domain.usecase.GetPurchasableCurrencies
import com.example.demo.domain.usecase.InsertCurrencies
import javax.inject.Inject

class DemoViewModelFactory @Inject constructor(
    private val clearDatabase: ClearDatabase,
    private val insertCurrencies: InsertCurrencies,
    private val getCryptoCurrencies: GetCryptoCurrencies,
    private val getFiatCurrencies: GetFiatCurrencies,
    private val getPurchasableCurrencies: GetPurchasableCurrencies
) : ViewModelProvider.Factory {
    
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DemoViewModel::class.java)) {
            return DemoViewModel(
                clearDatabase,
                insertCurrencies,
                getCryptoCurrencies,
                getFiatCurrencies,
                getPurchasableCurrencies
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
