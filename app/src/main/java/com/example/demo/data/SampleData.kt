package com.example.demo.data

import android.content.Context
import com.example.demo.data.local.entity.CurrencyEntity
import com.example.demo.data.mapper.toDomain
import com.example.demo.domain.model.CurrencyInfo
import kotlinx.serialization.json.Json

object SampleData {
    
    private var cryptoData: List<CurrencyEntity>? = null
    private var fiatData: List<CurrencyEntity>? = null
    
    private val purchasableIds = listOf("BTC", "ETH", "USDC")
    
    private val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
        encodeDefaults = true
    }
    
    fun init(context: Context) {
        try {
            if (cryptoData == null) {
                val cryptoJson = context.assets.open("crypto_currencies.json").bufferedReader().use { it.readText() }
                cryptoData = json.decodeFromString<List<CurrencyEntity>>(cryptoJson)
            }
            
            if (fiatData == null) {
                val fiatJson = context.assets.open("fiat_currencies.json").bufferedReader().use { it.readText() }
                fiatData = json.decodeFromString<List<CurrencyEntity>>(fiatJson)
            }
        } catch (e: Exception) {
            throw e
        }
    }
    
    val cryptoCurrencies: List<CurrencyInfo>
        get() {
            return cryptoData?.toDomain() ?: emptyList()
        }
    
    val fiatCurrencies: List<CurrencyInfo>
        get() {
            return fiatData?.toDomain() ?: emptyList()
        }
    
    fun getPurchasableCurrencies(): List<CurrencyInfo> {
        val purchasable = mutableListOf<CurrencyInfo>()
        
        purchasable.addAll(cryptoCurrencies.filter { it.id in purchasableIds })
        purchasable.addAll(fiatCurrencies)
        return purchasable
    }
}
