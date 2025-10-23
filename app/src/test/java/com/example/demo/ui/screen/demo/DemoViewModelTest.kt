package com.example.demo.ui.screen.demo

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.demo.data.repository.CurrencyRepository
import com.example.demo.domain.model.CurrencyInfo
import io.mockk.coEvery
import io.mockk.coVerify
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
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DemoViewModelTest {
    
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    
    private val testDispatcher = StandardTestDispatcher()
    private lateinit var repository: CurrencyRepository
    private lateinit var viewModel: DemoViewModel
    
    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = mockk(relaxed = true)
        viewModel = DemoViewModel(repository)
    }
    
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
    
    @Test
    fun `clearDatabase calls repository clearAllCurrencies`() = runTest {
        coEvery { repository.clearAllCurrencies() } returns Unit
        
        viewModel.clearDatabase()
        advanceUntilIdle()
        
        coVerify { repository.clearAllCurrencies() }
    }
    
    @Test
    fun `clearDatabase success shows success message`() = runTest {
        coEvery { repository.clearAllCurrencies() } returns Unit
        
        viewModel.clearDatabase()
        advanceUntilIdle()
        
        assertNotNull(viewModel.message.value)
        assertEquals("Database cleared successfully", viewModel.message.value)
    }
    
    @Test
    fun `insertData fetches from API and saves to database`() = runTest {
        val cryptoCurrencies = listOf(CurrencyInfo("BTC", "Bitcoin", "BTC"))
        val fiatCurrencies = listOf(CurrencyInfo("USD", "US Dollar", "$", "USD"))
        
        coEvery { repository.fetchCryptoCurrenciesFromAPI() } returns cryptoCurrencies
        coEvery { repository.fetchFiatCurrenciesFromAPI() } returns fiatCurrencies
        coEvery { repository.insertCurrencies(any()) } returns Unit
        
        viewModel.insertData()
        advanceUntilIdle()
        
        coVerify { repository.fetchCryptoCurrenciesFromAPI() }
        coVerify { repository.fetchFiatCurrenciesFromAPI() }
        coVerify { repository.insertCurrencies(any()) }
    }
    
    @Test
    fun `loadCryptoCurrencies loads from database and filters crypto`() = runTest {
        val allCurrencies = listOf(
            CurrencyInfo("BTC", "Bitcoin", "BTC"),
            CurrencyInfo("USD", "US Dollar", "$", "USD")
        )
        every { repository.getAllCurrenciesFromDatabase() } returns flowOf(allCurrencies)
        
        viewModel.loadCryptoCurrencies()
        advanceUntilIdle()
        
        assertEquals(1, viewModel.loadedCurrencies.value?.size)
        assertEquals("BTC", viewModel.loadedCurrencies.value?.get(0)?.id)
    }
    
    @Test
    fun `loadFiatCurrencies loads from database and filters fiat`() = runTest {
        val allCurrencies = listOf(
            CurrencyInfo("BTC", "Bitcoin", "BTC"),
            CurrencyInfo("USD", "US Dollar", "$", "USD")
        )
        every { repository.getAllCurrenciesFromDatabase() } returns flowOf(allCurrencies)
        
        viewModel.loadFiatCurrencies()
        advanceUntilIdle()
        
        assertEquals(1, viewModel.loadedCurrencies.value?.size)
        assertEquals("USD", viewModel.loadedCurrencies.value?.get(0)?.id)
    }
    
    @Test
    fun `loadPurchasableCurrencies fetches from API`() = runTest {
        val testCurrencies = listOf(CurrencyInfo("BTC", "Bitcoin", "BTC"))
        coEvery { repository.fetchPurchasableCurrenciesFromAPI() } returns testCurrencies
        
        viewModel.loadPurchasableCurrencies()
        advanceUntilIdle()
        
        coVerify { repository.fetchPurchasableCurrenciesFromAPI() }
        assertEquals(testCurrencies, viewModel.loadedCurrencies.value)
    }
    
    @Test
    fun `clearMessage sets message to null`() = runTest {
        coEvery { repository.clearAllCurrencies() } returns Unit
        
        viewModel.clearDatabase()
        advanceUntilIdle()
        assertNotNull(viewModel.message.value)
        
        viewModel.clearMessage()
        
        assertNull(viewModel.message.value)
    }
}
