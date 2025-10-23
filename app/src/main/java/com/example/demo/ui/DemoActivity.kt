package com.example.demo.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.demo.CurrencyApplication
import com.example.demo.ui.navigation.CurrencyApp
import com.example.demo.ui.screen.demo.DemoViewModelFactory
import javax.inject.Inject

class DemoActivity : ComponentActivity() {
    
    @Inject
    lateinit var demoViewModelFactory: DemoViewModelFactory
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        (application as CurrencyApplication).appComponent.inject(this)
        
        enableEdgeToEdge()
        
        setContent {
            CurrencyApp(
                demoViewModelFactory = demoViewModelFactory
            )
        }
    }
}
