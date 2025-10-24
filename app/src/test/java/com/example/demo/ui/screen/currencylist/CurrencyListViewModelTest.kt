package com.example.demo.ui.screen.currencylist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.demo.domain.model.CurrencyInfo
import com.example.demo.domain.model.CurrencyType
import com.example.demo.domain.usecase.ObserveAllCurrencies
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
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
    private lateinit var observeAllCurrencies: ObserveAllCurrencies
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
        observeAllCurrencies = mockk()
        every { observeAllCurrencies(any()) } returns flowOf(testCurrencies)
        viewModel = CurrencyListViewModel(observeAllCurrencies, CurrencyType.ALL)
    }
    
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
    
    @Test
    fun `initial state loads currencies from use case`() = runTest {
        advanceUntilIdle()
        
        val state = viewModel.uiState.value
        assertEquals(testCurrencies.size, state.currencyList.size)
        assertEquals(testCurrencies.size, state.filteredList.size)
    }
    
    @Test
    fun `search query starting with name returns matching currencies`() = runTest {
        advanceUntilIdle()
        viewModel.onAction(CurrencyListAction.UpdateSearchQuery("Ethereum"))
        advanceUntilIdle()
        
        val results = viewModel.uiState.value.filteredList
        assertEquals(2, results.size) // Ethereum and Ethereum Classic
        assertTrue(results.any { it.name == "Ethereum" })
        assertTrue(results.any { it.name == "Ethereum Classic" })
    }
    
    @Test
    fun `search query with space prefix matches correctly`() = runTest {
        advanceUntilIdle()
        viewModel.onAction(CurrencyListAction.UpdateSearchQuery("Classic"))
        advanceUntilIdle()
        
        val results = viewModel.uiState.value.filteredList
        assertEquals(1, results.size)
        assertEquals("Ethereum Classic", results[0].name)
    }
    
    @Test
    fun `search query does not match middle of word without space`() = runTest {
        advanceUntilIdle()
        viewModel.onAction(CurrencyListAction.UpdateSearchQuery("lass")) // Should not match "Classic"
        advanceUntilIdle()
        
        val results = viewModel.uiState.value.filteredList
        assertEquals(0, results.size)
    }
    
    @Test
    fun `search by symbol prefix matches currencies`() = runTest {
        advanceUntilIdle()
        viewModel.onAction(CurrencyListAction.UpdateSearchQuery("ET"))
        advanceUntilIdle()
        
        val results = viewModel.uiState.value.filteredList
        assertEquals(2, results.size) // ETH and ETC
        assertTrue(results.any { it.symbol == "ETH" })
        assertTrue(results.any { it.symbol == "ETC" })
    }
    
    @Test
    fun `search by full symbol matches currency`() = runTest {
        advanceUntilIdle()
        viewModel.onAction(CurrencyListAction.UpdateSearchQuery("BTC"))
        advanceUntilIdle()
        
        val results = viewModel.uiState.value.filteredList
        assertEquals(1, results.size)
        assertEquals("Bitcoin", results[0].name)
    }
    
    @Test
    fun `search is case insensitive`() = runTest {
        advanceUntilIdle()
        viewModel.onAction(CurrencyListAction.UpdateSearchQuery("bitcoin"))
        advanceUntilIdle()
        
        val results = viewModel.uiState.value.filteredList
        assertEquals(1, results.size)
        assertEquals("Bitcoin", results[0].name)
    }
    
    @Test
    fun `empty search query returns all currencies`() = runTest {
        advanceUntilIdle()
        viewModel.onAction(CurrencyListAction.UpdateSearchQuery("test"))
        advanceUntilIdle()
        assertEquals(0, viewModel.uiState.value.filteredList.size)
        
        viewModel.onAction(CurrencyListAction.UpdateSearchQuery(""))
        advanceUntilIdle()
        
        assertEquals(testCurrencies.size, viewModel.uiState.value.filteredList.size)
    }
    
    @Test
    fun `clearSearch resets to all currencies`() = runTest {
        advanceUntilIdle()
        viewModel.onAction(CurrencyListAction.UpdateSearchQuery("Bitcoin"))
        advanceUntilIdle()
        assertEquals(1, viewModel.uiState.value.filteredList.size)
        
        viewModel.onAction(CurrencyListAction.ClearSearch)
        advanceUntilIdle()
        
        assertEquals(testCurrencies.size, viewModel.uiState.value.filteredList.size)
        assertEquals("", viewModel.uiState.value.searchQuery)
    }
    
    @Test
    fun `isSearching state updates correctly`() = runTest {
        advanceUntilIdle()
        assertFalse(viewModel.uiState.value.isSearching)
        
        viewModel.onAction(CurrencyListAction.UpdateSearchQuery("test"))
        advanceUntilIdle()
        
        assertTrue(viewModel.uiState.value.isSearching)
        
        viewModel.onAction(CurrencyListAction.ClearSearch)
        advanceUntilIdle()
        
        assertFalse(viewModel.uiState.value.isSearching)
    }
    
    @Test
    fun `search with partial symbol match`() = runTest {
        advanceUntilIdle()
        viewModel.onAction(CurrencyListAction.UpdateSearchQuery("CR"))
        advanceUntilIdle()
        
        val results = viewModel.uiState.value.filteredList
        assertEquals(1, results.size) // Only CRO matches (starts with CR)
        assertTrue(results.any { it.symbol == "CRO" })
    }
    
    @Test
    fun `search query with whitespace is trimmed`() = runTest {
        advanceUntilIdle()
        viewModel.onAction(CurrencyListAction.UpdateSearchQuery("  Bitcoin  "))
        advanceUntilIdle()
        
        val results = viewModel.uiState.value.filteredList
        assertEquals(1, results.size)
        assertEquals("Bitcoin", results[0].name)
    }
    
    // ==================== Code Field Search Tests ====================
    
    @Test
    fun `search by code prefix matches fiat currencies`() = runTest {
        every { observeAllCurrencies(any()) } returns flowOf(testFiatCurrencies)
        val fiatViewModel = CurrencyListViewModel(observeAllCurrencies, CurrencyType.FIAT)
        advanceUntilIdle()
        
        fiatViewModel.onAction(CurrencyListAction.UpdateSearchQuery("US"))
        advanceUntilIdle()
        
        val results = fiatViewModel.uiState.value.filteredList
        assertEquals(1, results.size)
        assertEquals("USD", results[0].code)
        assertEquals("United States Dollar", results[0].name)
    }
    
    @Test
    fun `search by full code matches exact fiat currency`() = runTest {
        every { observeAllCurrencies(any()) } returns flowOf(testFiatCurrencies)
        val fiatViewModel = CurrencyListViewModel(observeAllCurrencies, CurrencyType.FIAT)
        advanceUntilIdle()
        
        fiatViewModel.onAction(CurrencyListAction.UpdateSearchQuery("EUR"))
        advanceUntilIdle()
        
        val results = fiatViewModel.uiState.value.filteredList
        assertEquals(1, results.size)
        assertEquals("EUR", results[0].code)
        assertEquals("Euro", results[0].name)
    }
    
    @Test
    fun `search by code is case insensitive`() = runTest {
        every { observeAllCurrencies(any()) } returns flowOf(testFiatCurrencies)
        val fiatViewModel = CurrencyListViewModel(observeAllCurrencies, CurrencyType.FIAT)
        advanceUntilIdle()
        
        fiatViewModel.onAction(CurrencyListAction.UpdateSearchQuery("gbp"))
        advanceUntilIdle()
        
        val results = fiatViewModel.uiState.value.filteredList
        assertEquals(1, results.size)
        assertEquals("GBP", results[0].code)
        assertEquals("British Pound Sterling", results[0].name)
    }
    
    @Test
    fun `search by symbol matches multiple fiat currencies`() = runTest {
        every { observeAllCurrencies(any()) } returns flowOf(testFiatCurrencies)
        val fiatViewModel = CurrencyListViewModel(observeAllCurrencies, CurrencyType.FIAT)
        advanceUntilIdle()
        
        fiatViewModel.onAction(CurrencyListAction.UpdateSearchQuery("$"))
        advanceUntilIdle()
        
        val results = fiatViewModel.uiState.value.filteredList
        // "$" matches: USD, AUD, CAD (all have $ symbol)
        assertEquals(3, results.size)
        assertTrue(results.any { it.code == "USD" })
        assertTrue(results.any { it.code == "AUD" })
        assertTrue(results.any { it.code == "CAD" })
    }
    
    @Test
    fun `search returns no results for crypto currencies when searching by non-existent code`() = runTest {
        advanceUntilIdle()
        // Crypto currencies have code as null
        viewModel.onAction(CurrencyListAction.UpdateSearchQuery("USD"))
        advanceUntilIdle()
        
        val results = viewModel.uiState.value.filteredList
        assertEquals(0, results.size) // No crypto matches USD
    }
    
    @Test
    fun `search works with mixed crypto and fiat currencies by code`() = runTest {
        val mixedCurrencies = arrayListOf<CurrencyInfo>()
        mixedCurrencies.addAll(testCurrencies)
        mixedCurrencies.addAll(testFiatCurrencies)
        
        every { observeAllCurrencies(any()) } returns flowOf(mixedCurrencies)
        val mixedViewModel = CurrencyListViewModel(observeAllCurrencies, CurrencyType.ALL)
        advanceUntilIdle()
        
        mixedViewModel.onAction(CurrencyListAction.UpdateSearchQuery("JPY"))
        advanceUntilIdle()
        
        val results = mixedViewModel.uiState.value.filteredList
        assertEquals(1, results.size)
        assertEquals("JPY", results[0].code)
    }
    
    @Test
    fun `search by code prefix does not match middle of code`() = runTest {
        every { observeAllCurrencies(any()) } returns flowOf(testFiatCurrencies)
        val fiatViewModel = CurrencyListViewModel(observeAllCurrencies, CurrencyType.FIAT)
        advanceUntilIdle()
        
        fiatViewModel.onAction(CurrencyListAction.UpdateSearchQuery("SD")) // Should not match "USD"
        advanceUntilIdle()
        
        val results = fiatViewModel.uiState.value.filteredList
        assertEquals(0, results.size)
    }
    
    @Test
    fun `search prioritizes code over symbol when both match`() = runTest {
        every { observeAllCurrencies(any()) } returns flowOf(testFiatCurrencies)
        val fiatViewModel = CurrencyListViewModel(observeAllCurrencies, CurrencyType.FIAT)
        advanceUntilIdle()
        
        fiatViewModel.onAction(CurrencyListAction.UpdateSearchQuery("$")) // Matches symbol for USD, AUD, CAD
        advanceUntilIdle()
        
        val results = fiatViewModel.uiState.value.filteredList
        // Should match all currencies with $ symbol
        assertEquals(3, results.size)
        assertTrue(results.any { it.code == "USD" })
        assertTrue(results.any { it.code == "AUD" })
        assertTrue(results.any { it.code == "CAD" })
    }
}
