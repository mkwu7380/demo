package com.example.demo.ui.viewmodel.currencylist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.demo.domain.model.CurrencyType
import com.example.demo.domain.usecase.ObserveAllCurrencies
import javax.inject.Inject

class CurrencyListViewModelFactory @Inject constructor(
    private val observeAllCurrencies: ObserveAllCurrencies
) {
    
    fun create(currencyType: CurrencyType): ViewModelProvider.Factory {
        return object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(CurrencyListViewModel::class.java)) {
                    return CurrencyListViewModel(
                        observeAllCurrencies,
                        currencyType
                    ) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
    }
}
