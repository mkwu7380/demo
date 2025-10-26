package com.example.demo.ui

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ComposeView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.demo.CurrencyApplication
import com.example.demo.R
import com.example.demo.domain.model.CurrencyType
import com.example.demo.ui.screen.currencylist.CurrencyListFragment
import com.example.demo.ui.screen.demo.DemoScreen
import com.example.demo.ui.viewmodel.demo.DemoAction
import com.example.demo.ui.viewmodel.demo.DemoViewModel
import com.example.demo.ui.viewmodel.demo.DemoViewModelFactory
import kotlinx.coroutines.launch
import javax.inject.Inject

class DemoActivity : AppCompatActivity() {
    
    @Inject
    lateinit var demoViewModelFactory: DemoViewModelFactory
    
    private val demoViewModel: DemoViewModel by viewModels { demoViewModelFactory }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        (application as CurrencyApplication).appComponent.inject(this)
        
        enableEdgeToEdge()
        
        setContentView(R.layout.activity_demo)
        
        val composeView = findViewById<ComposeView>(R.id.compose_view)
        composeView.setContent {
            MaterialTheme {
                val uiState by demoViewModel.uiState.collectAsStateWithLifecycle()
                
                DemoScreen(
                    uiState = uiState,
                    onAction = demoViewModel::onAction
                )
            }
        }
        
        observeLoadedCurrencies()
        setupFragmentBackStackListener()
    }
    
    private fun setupFragmentBackStackListener() {
        supportFragmentManager.addOnBackStackChangedListener {
            if (supportFragmentManager.backStackEntryCount == 0) {
                // Restore compose view when fragment is popped
                findViewById<ComposeView>(R.id.compose_view).visibility = View.VISIBLE
                findViewById<View>(R.id.fragment_container).visibility = View.GONE
            }
        }
    }
    
    private fun observeLoadedCurrencies() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                demoViewModel.uiState.collect { state ->
                    if (state.loadedCurrencies != null && state.currencyType != null) {
                        navigateToCurrencyList(state.currencyType)
                        demoViewModel.onAction(DemoAction.ClearLoadedCurrencies)
                    }
                }
            }
        }
    }
    
    private fun navigateToCurrencyList(currencyType: CurrencyType) {
        val composeView = findViewById<ComposeView>(R.id.compose_view)
        val fragmentContainer = findViewById<View>(R.id.fragment_container)
        
        // Hide compose view and show fragment container
        composeView.visibility = View.GONE
        fragmentContainer.visibility = View.VISIBLE
        
        val fragment = CurrencyListFragment.newInstance(currencyType)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }
}
