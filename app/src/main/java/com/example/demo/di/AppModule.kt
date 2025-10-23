package com.example.demo.di

import android.content.Context
import androidx.room.Room
import com.example.demo.data.local.CurrencyDao
import com.example.demo.data.local.CurrencyDatabase
import com.example.demo.data.repository.CurrencyRepositoryImpl
import com.example.demo.domain.repository.CurrencyRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [AppModule.BindsModule::class])
class AppModule(private val context: Context) {

    @Provides
    @Singleton
    fun provideContext(): Context {
        return context
    }

    @Provides
    @Singleton
    fun provideCurrencyDatabase(context: Context): CurrencyDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            CurrencyDatabase::class.java,
            CurrencyDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideCurrencyDao(database: CurrencyDatabase): CurrencyDao {
        return database.currencyDao()
    }

    @Module
    interface BindsModule {
        
        @Binds
        @Singleton
        fun bindCurrencyRepository(
            impl: CurrencyRepositoryImpl
        ): CurrencyRepository
        
        @Binds
        fun bindClearDatabase(
            impl: com.example.demo.domain.usecase.ClearDatabaseImpl
        ): com.example.demo.domain.usecase.ClearDatabase
        
        @Binds
        fun bindGetCryptoCurrencies(
            impl: com.example.demo.domain.usecase.GetCryptoCurrenciesImpl
        ): com.example.demo.domain.usecase.GetCryptoCurrencies
        
        @Binds
        fun bindGetFiatCurrencies(
            impl: com.example.demo.domain.usecase.GetFiatCurrenciesImpl
        ): com.example.demo.domain.usecase.GetFiatCurrencies
        
        @Binds
        fun bindGetPurchasableCurrencies(
            impl: com.example.demo.domain.usecase.GetPurchasableCurrenciesImpl
        ): com.example.demo.domain.usecase.GetPurchasableCurrencies
        
        @Binds
        fun bindInsertCurrencies(
            impl: com.example.demo.domain.usecase.InsertCurrenciesImpl
        ): com.example.demo.domain.usecase.InsertCurrencies
    }
}
