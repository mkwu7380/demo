package com.example.demo.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.demo.data.local.entity.CurrencyEntity

@Database(
    entities = [CurrencyEntity::class],
    version = 1,
    exportSchema = false
)
abstract class CurrencyDatabase : RoomDatabase() {
    abstract fun currencyDao(): CurrencyDao
    
    companion object {
        const val DATABASE_NAME = "currency_database"
    }
}
