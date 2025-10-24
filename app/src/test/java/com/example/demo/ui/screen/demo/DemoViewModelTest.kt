package com.example.demo.ui.screen.demo

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.demo.domain.model.CurrencyInfo
import com.example.demo.domain.model.CurrencyType
import com.example.demo.domain.usecase.ClearDatabase
import com.example.demo.domain.usecase.GetCryptoCurrencies
import com.example.demo.domain.usecase.GetFiatCurrencies
import com.example.demo.domain.usecase.GetPurchasableCurrencies
import com.example.demo.domain.usecase.InsertCurrencies
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
    private lateinit var clearDatabase: ClearDatabase
    private lateinit var insertCurrencies: InsertCurrencies
    private lateinit var getCryptoCurrencies: GetCryptoCurrencies
    private lateinit var getFiatCurrencies: GetFiatCurrencies
    private lateinit var getPurchasableCurrencies: GetPurchasableCurrencies
    private lateinit var viewModel: DemoViewModel
    
    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        clearDatabase = mockk(relaxed = true)
        insertCurrencies = mockk(relaxed = true)
        getCryptoCurrencies = mockk(relaxed = true)
        getFiatCurrencies = mockk(relaxed = true)
        getPurchasableCurrencies = mockk(relaxed = true)
        viewModel = DemoViewModel(
            clearDatabase,
            insertCurrencies,
            getCryptoCurrencies,
            getFiatCurrencies,
            getPurchasableCurrencies
        )
    }
    
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
    
    @Test
    fun `clearDatabase calls clearDatabase use case`() = runTest {
        coEvery { clearDatabase.invoke() } returns Unit
        
        viewModel.onAction(DemoAction.ClearDatabase)
        advanceUntilIdle()
        
        coVerify { clearDatabase.invoke() }
    }
    
    @Test
    fun `clearDatabase success shows success message`() = runTest {
        coEvery { clearDatabase.invoke() } returns Unit
        
        viewModel.onAction(DemoAction.ClearDatabase)
        advanceUntilIdle()
        
        assertNotNull(viewModel.uiState.value.message)
        assertEquals("Database cleared successfully", viewModel.uiState.value.message)
    }
    
    @Test
    fun `insertData calls insertCurrencies use case`() = runTest {
        coEvery { insertCurrencies() } returns Unit
        
        viewModel.onAction(DemoAction.InsertData)
        advanceUntilIdle()
        
        coVerify { insertCurrencies() }
    }
    
    @Test
    fun `insertData success shows success message`() = runTest {
        coEvery { insertCurrencies() } returns Unit
        
        viewModel.onAction(DemoAction.InsertData)
        advanceUntilIdle()
        
        assertNotNull(viewModel.uiState.value.message)
        assertEquals("Currencies inserted into database successfully", viewModel.uiState.value.message)
    }
    
    @Test
    fun `loadCryptoCurrencies calls getCryptoCurrencies use case`() = runTest {
        val cryptoCurrencies = listOf(
            CurrencyInfo("BTC", "Bitcoin", "BTC"),
            CurrencyInfo("ETH", "Ethereum", "ETH")
        )
        coEvery { getCryptoCurrencies() } returns cryptoCurrencies
        
        viewModel.onAction(DemoAction.LoadCryptoCurrencies)
        advanceUntilIdle()
        
        coVerify { getCryptoCurrencies() }
        assertEquals(cryptoCurrencies, viewModel.uiState.value.loadedCurrencies)
        assertEquals(CurrencyType.CRYPTO, viewModel.uiState.value.currencyType)
    }
    
    @Test
    fun `loadFiatCurrencies calls getFiatCurrencies use case`() = runTest {
        val fiatCurrencies = listOf(
            CurrencyInfo("USD", "US Dollar", "$", "USD"),
            CurrencyInfo("EUR", "Euro", "€", "EUR")
        )
        coEvery { getFiatCurrencies() } returns fiatCurrencies
        
        viewModel.onAction(DemoAction.LoadFiatCurrencies)
        advanceUntilIdle()
        
        coVerify { getFiatCurrencies() }
        assertEquals(fiatCurrencies, viewModel.uiState.value.loadedCurrencies)
        assertEquals(CurrencyType.FIAT, viewModel.uiState.value.currencyType)
    }
    
    @Test
    fun `loadPurchasableCurrencies calls getPurchasableCurrencies use case`() = runTest {
        val testCurrencies = listOf(
            CurrencyInfo("BTC", "Bitcoin", "BTC"),
            CurrencyInfo("USD", "US Dollar", "$", "USD")
        )
        coEvery { getPurchasableCurrencies() } returns testCurrencies
        
        viewModel.onAction(DemoAction.LoadPurchasableCurrencies)
        advanceUntilIdle()
        
        coVerify { getPurchasableCurrencies() }
        assertEquals(testCurrencies, viewModel.uiState.value.loadedCurrencies)
        assertEquals(CurrencyType.ALL, viewModel.uiState.value.currencyType)
    }
    
    @Test
    fun `clearMessage sets message to null`() = runTest {
        coEvery { clearDatabase.invoke() } returns Unit
        
        viewModel.onAction(DemoAction.ClearDatabase)
        advanceUntilIdle()
        assertNotNull(viewModel.uiState.value.message)
        
        viewModel.onAction(DemoAction.ClearMessage)
        advanceUntilIdle()
        
        assertNull(viewModel.uiState.value.message)
    }
    
    @Test
    fun `clearLoadedCurrencies clears loaded currencies and type`() = runTest {
        val testCurrencies = listOf(CurrencyInfo("BTC", "Bitcoin", "BTC"))
        coEvery { getCryptoCurrencies() } returns testCurrencies
        
        viewModel.onAction(DemoAction.LoadCryptoCurrencies)
        advanceUntilIdle()
        assertNotNull(viewModel.uiState.value.loadedCurrencies)
        assertNotNull(viewModel.uiState.value.currencyType)
        
        viewModel.onAction(DemoAction.ClearLoadedCurrencies)
        advanceUntilIdle()
        
        assertNull(viewModel.uiState.value.loadedCurrencies)
        assertNull(viewModel.uiState.value.currencyType)
    }
}
