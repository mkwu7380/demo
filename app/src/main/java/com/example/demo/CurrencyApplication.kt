package com.example.demo

import android.app.Application
import com.example.demo.data.SampleData
import com.example.demo.di.AppComponent
import com.example.demo.di.AppModule
import com.example.demo.di.DaggerAppComponent

class CurrencyApplication : Application() {
    
    lateinit var appComponent: AppComponent
        private set
    
    override fun onCreate() {
        super.onCreate()
        
        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build()
        
        SampleData.init(this)
    }
}
