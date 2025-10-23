package com.example.demo.di

import com.example.demo.ui.DemoActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {
    
    fun inject(activity: DemoActivity)
}
