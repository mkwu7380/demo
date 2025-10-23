package com.example.demo.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.demo.data.local.entity.CurrencyEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CurrencyDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCurrencies(currencies: List<CurrencyEntity>)
    
    @Query("SELECT * FROM currencies")
    fun getAllCurrencies(): Flow<List<CurrencyEntity>>
    
    @Query("DELETE FROM currencies")
    suspend fun clearAll()
    
    @Query("SELECT * FROM currencies WHERE id IN (:ids)")
    fun getCurrenciesByIds(ids: List<String>): Flow<List<CurrencyEntity>>
}
