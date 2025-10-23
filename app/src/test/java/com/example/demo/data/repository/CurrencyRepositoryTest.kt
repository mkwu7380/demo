package com.example.demo.data.repository

import android.content.Context
import android.content.res.AssetManager
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.demo.data.SampleData
import com.example.demo.data.local.CurrencyDao
import com.example.demo.domain.model.CurrencyInfo
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.ByteArrayInputStream

@OptIn(ExperimentalCoroutinesApi::class)
class CurrencyRepositoryTest {
    
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    
    private lateinit var dao: CurrencyDao
    private lateinit var repository: CurrencyRepository
    
    @Before
    fun setup() {
        dao = mockk(relaxed = true)
        repository = CurrencyRepository(dao)
        
        // Initialize SampleData with mock context for tests
        val mockContext = mockk<Context>(relaxed = true)
        val mockAssetManager = mockk<AssetManager>(relaxed = true)
        
        val cryptoJson = """
            [
              {"id": "BTC", "name": "Bitcoin", "symbol": "BTC"},
              {"id": "ETH", "name": "Ethereum", "symbol": "ETH"},
              {"id": "USDC", "name": "USD Coin", "symbol": "USDC"}
            ]
        """.trimIndent()
        
        val fiatJson = """
            [
              {"id": "USD", "name": "US Dollar", "symbol": "$", "code": "USD"},
              {"id": "EUR", "name": "Euro", "symbol": "€", "code": "EUR"},
              {"id": "SGD", "name": "Singapore Dollar", "symbol": "$", "code": "SGD"}
            ]
        """.trimIndent()
        
        every { mockContext.assets } returns mockAssetManager
        every { mockAssetManager.open("crypto_currencies.json") } returns ByteArrayInputStream(cryptoJson.toByteArray())
        every { mockAssetManager.open("fiat_currencies.json") } returns ByteArrayInputStream(fiatJson.toByteArray())
        
        SampleData.init(mockContext)
    }
    
    @Test
    fun `getAllCurrenciesFromDatabase returns flow from dao`() = runTest {
        val testCurrencies = listOf(
            CurrencyInfo("BTC", "Bitcoin", "BTC"),
            CurrencyInfo("ETH", "Ethereum", "ETH")
        )
        every { dao.getAllCurrencies() } returns flowOf(testCurrencies)
        
        val result = repository.getAllCurrenciesFromDatabase().first()
        assertEquals(testCurrencies, result)
    }
    
    @Test
    fun `insertCurrencies calls dao insert`() = runTest {
        val testCurrencies = listOf(
            CurrencyInfo("BTC", "Bitcoin", "BTC")
        )
        coEvery { dao.insertCurrencies(testCurrencies) } returns Unit
        
        repository.insertCurrencies(testCurrencies)
        
        coVerify { dao.insertCurrencies(testCurrencies) }
    }
    
    @Test
    fun `clearAllCurrencies calls dao clearAll`() = runTest {
        coEvery { dao.clearAll() } returns Unit
        
        repository.clearAllCurrencies()
        
        coVerify { dao.clearAll() }
    }
    
    @Test
    fun `fetchCryptoCurrenciesFromAPI returns crypto data from sample`() = runTest {
        val result = repository.fetchCryptoCurrenciesFromAPI()
        
        assert(result.isNotEmpty())
        assertEquals(3, result.size)
    }
    
    @Test
    fun `fetchFiatCurrenciesFromAPI returns fiat data from sample`() = runTest {
        val result = repository.fetchFiatCurrenciesFromAPI()
        
        assert(result.isNotEmpty())
        assertEquals(3, result.size)
    }
    
    @Test
    fun `fetchPurchasableCurrenciesFromAPI returns correct currencies`() = runTest {
        val result = repository.fetchPurchasableCurrenciesFromAPI()
        
        assert(result.isNotEmpty())
        assert(result.any { it.code != null })
    }
    
    @Test
    fun `crypto currencies contain expected entries`() = runTest {
        val result = repository.fetchCryptoCurrenciesFromAPI()
        
        assert(result.any { it.id == "BTC" })
        assert(result.any { it.id == "ETH" })
        assert(result.any { it.id == "USDC" })
    }
    
    @Test
    fun `fiat currencies contain expected entries`() = runTest {
        val result = repository.fetchFiatCurrenciesFromAPI()
        
        assert(result.any { it.id == "USD" })
        assert(result.any { it.id == "EUR" })
        assert(result.any { it.id == "SGD" })
    }
}
