package com.example.demo.di

import com.example.demo.ui.DemoActivity
import com.example.demo.ui.screen.currencylist.CurrencyListFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {
    
    fun inject(activity: DemoActivity)
    
    fun inject(fragment: CurrencyListFragment)
}
