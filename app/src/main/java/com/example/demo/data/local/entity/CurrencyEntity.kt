package com.example.demo.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "currencies")
data class CurrencyEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val symbol: String,
    val code: String? = null
)
