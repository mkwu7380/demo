package com.example.demo.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CurrencyInfo(
    val id: String,
    val name: String,
    val symbol: String,
    val code: String? = null
) : Parcelable
