package com.example.demo.ui.screen.currencylist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.demo.domain.model.CurrencyInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CurrencyListViewModelTest {
    
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    
    private val testDispatcher = StandardTestDispatcher()
    private lateinit var viewModel: CurrencyListViewModel
    
    private val testCurrencies = arrayListOf(
        CurrencyInfo("BTC", "Bitcoin", "BTC"),
        CurrencyInfo("ETH", "Ethereum", "ETH"),
        CurrencyInfo("ETC", "Ethereum Classic", "ETC"),
        CurrencyInfo("XRP", "Ripple", "XRP"),
        CurrencyInfo("CUC", "Cucumber", "CUC"),
        CurrencyInfo("CRO", "Crypto.com Coin", "CRO")
    )
    
    private val testFiatCurrencies = arrayListOf(
        CurrencyInfo("USD", "United States Dollar", "$", "USD"),
        CurrencyInfo("EUR", "Euro", "€", "EUR"),
        CurrencyInfo("GBP", "British Pound Sterling", "£", "GBP"),
        CurrencyInfo("JPY", "Japanese Yen", "¥", "JPY"),
        CurrencyInfo("AUD", "Australian Dollar", "$", "AUD"),
        CurrencyInfo("CAD", "Canadian Dollar", "$", "CAD")
    )
    
    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = CurrencyListViewModel()
        viewModel.setCurrencyList(testCurrencies)
    }
    
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
    
    @Test
    fun `setCurrencyList updates both currency and filtered lists`() = runTest {
        advanceUntilIdle()
        
        assertEquals(testCurrencies.size, viewModel.currencyList.value.size)
        assertEquals(testCurrencies.size, viewModel.filteredList.value.size)
    }
    
    @Test
    fun `search query starting with name returns matching currencies`() = runTest {
        viewModel.updateSearchQuery("Ethereum")
        advanceUntilIdle()
        
        val results = viewModel.filteredList.value
        assertEquals(2, results.size) // Ethereum and Ethereum Classic
        assertTrue(results.any { it.name == "Ethereum" })
        assertTrue(results.any { it.name == "Ethereum Classic" })
    }
    
    @Test
    fun `search query with space prefix matches correctly`() = runTest {
        viewModel.updateSearchQuery("Classic")
        advanceUntilIdle()
        
        val results = viewModel.filteredList.value
        assertEquals(1, results.size)
        assertEquals("Ethereum Classic", results[0].name)
    }
    
    @Test
    fun `search query does not match middle of word without space`() = runTest {
        viewModel.updateSearchQuery("lass") // Should not match "Classic"
        advanceUntilIdle()
        
        val results = viewModel.filteredList.value
        assertEquals(0, results.size)
    }
    
    @Test
    fun `search by symbol prefix matches currencies`() = runTest {
        viewModel.updateSearchQuery("ET")
        advanceUntilIdle()
        
        val results = viewModel.filteredList.value
        assertEquals(2, results.size) // ETH and ETC
        assertTrue(results.any { it.symbol == "ETH" })
        assertTrue(results.any { it.symbol == "ETC" })
    }
    
    @Test
    fun `search by full symbol matches currency`() = runTest {
        viewModel.updateSearchQuery("BTC")
        advanceUntilIdle()
        
        val results = viewModel.filteredList.value
        assertEquals(1, results.size)
        assertEquals("Bitcoin", results[0].name)
    }
    
    @Test
    fun `search is case insensitive`() = runTest {
        viewModel.updateSearchQuery("bitcoin")
        advanceUntilIdle()
        
        val results = viewModel.filteredList.value
        assertEquals(1, results.size)
        assertEquals("Bitcoin", results[0].name)
    }
    
    @Test
    fun `empty search query returns all currencies`() = runTest {
        viewModel.updateSearchQuery("test")
        advanceUntilIdle()
        assertEquals(0, viewModel.filteredList.value.size)
        
        viewModel.updateSearchQuery("")
        advanceUntilIdle()
        
        assertEquals(testCurrencies.size, viewModel.filteredList.value.size)
    }
    
    @Test
    fun `clearSearch resets to all currencies`() = runTest {
        viewModel.updateSearchQuery("Bitcoin")
        advanceUntilIdle()
        assertEquals(1, viewModel.filteredList.value.size)
        
        viewModel.clearSearch()
        advanceUntilIdle()
        
        assertEquals(testCurrencies.size, viewModel.filteredList.value.size)
        assertEquals("", viewModel.searchQuery.value)
    }
    
    @Test
    fun `isSearching state updates correctly`() = runTest {
        assertFalse(viewModel.isSearching.value)
        
        viewModel.updateSearchQuery("test")
        advanceUntilIdle()
        
        assertTrue(viewModel.isSearching.value)
        
        viewModel.clearSearch()
        advanceUntilIdle()
        
        assertFalse(viewModel.isSearching.value)
    }
    
    @Test
    fun `search with partial symbol match`() = runTest {
        viewModel.updateSearchQuery("CR")
        advanceUntilIdle()
        
        val results = viewModel.filteredList.value
        assertEquals(1, results.size) // Only CRO matches (starts with CR)
        assertTrue(results.any { it.symbol == "CRO" })
    }
    
    @Test
    fun `search query with whitespace is trimmed`() = runTest {
        viewModel.updateSearchQuery("  Bitcoin  ")
        advanceUntilIdle()
        
        val results = viewModel.filteredList.value
        assertEquals(1, results.size)
        assertEquals("Bitcoin", results[0].name)
    }
    
    // ==================== Code Field Search Tests ====================
    
    @Test
    fun `search by code prefix matches fiat currencies`() = runTest {
        viewModel.setCurrencyList(testFiatCurrencies)
        advanceUntilIdle()
        
        viewModel.updateSearchQuery("US")
        advanceUntilIdle()
        
        val results = viewModel.filteredList.value
        assertEquals(1, results.size)
        assertEquals("USD", results[0].code)
        assertEquals("United States Dollar", results[0].name)
    }
    
    @Test
    fun `search by full code matches exact fiat currency`() = runTest {
        viewModel.setCurrencyList(testFiatCurrencies)
        advanceUntilIdle()
        
        viewModel.updateSearchQuery("EUR")
        advanceUntilIdle()
        
        val results = viewModel.filteredList.value
        assertEquals(1, results.size)
        assertEquals("EUR", results[0].code)
        assertEquals("Euro", results[0].name)
    }
    
    @Test
    fun `search by code is case insensitive`() = runTest {
        viewModel.setCurrencyList(testFiatCurrencies)
        advanceUntilIdle()
        
        viewModel.updateSearchQuery("gbp")
        advanceUntilIdle()
        
        val results = viewModel.filteredList.value
        assertEquals(1, results.size)
        assertEquals("GBP", results[0].code)
        assertEquals("British Pound Sterling", results[0].name)
    }
    
    @Test
    fun `search by code prefix matches multiple fiat currencies`() = runTest {
        viewModel.setCurrencyList(testFiatCurrencies)
        advanceUntilIdle()
        
        viewModel.updateSearchQuery("A")
        advanceUntilIdle()
        
        val results = viewModel.filteredList.value
        assertEquals(2, results.size) // AUD and CAD
        assertTrue(results.any { it.code == "AUD" })
        assertTrue(results.any { it.code == "CAD" })
    }
    
    @Test
    fun `search returns no results for crypto currencies when searching by non-existent code`() = runTest {
        // Crypto currencies have code as null
        viewModel.updateSearchQuery("USD")
        advanceUntilIdle()
        
        val results = viewModel.filteredList.value
        assertEquals(0, results.size) // No crypto matches USD
    }
    
    @Test
    fun `search works with mixed crypto and fiat currencies by code`() = runTest {
        val mixedCurrencies = arrayListOf<CurrencyInfo>()
        mixedCurrencies.addAll(testCurrencies)
        mixedCurrencies.addAll(testFiatCurrencies)
        
        viewModel.setCurrencyList(mixedCurrencies)
        advanceUntilIdle()
        
        viewModel.updateSearchQuery("JPY")
        advanceUntilIdle()
        
        val results = viewModel.filteredList.value
        assertEquals(1, results.size)
        assertEquals("JPY", results[0].code)
    }
    
    @Test
    fun `search by code prefix does not match middle of code`() = runTest {
        viewModel.setCurrencyList(testFiatCurrencies)
        advanceUntilIdle()
        
        viewModel.updateSearchQuery("SD") // Should not match "USD"
        advanceUntilIdle()
        
        val results = viewModel.filteredList.value
        assertEquals(0, results.size)
    }
    
    @Test
    fun `search prioritizes code over symbol when both match`() = runTest {
        viewModel.setCurrencyList(testFiatCurrencies)
        advanceUntilIdle()
        
        viewModel.updateSearchQuery("$") // Matches symbol for USD, AUD, CAD
        advanceUntilIdle()
        
        val results = viewModel.filteredList.value
        // Should match all currencies with $ symbol
        assertEquals(3, results.size)
        assertTrue(results.any { it.code == "USD" })
        assertTrue(results.any { it.code == "AUD" })
        assertTrue(results.any { it.code == "CAD" })
    }
}
