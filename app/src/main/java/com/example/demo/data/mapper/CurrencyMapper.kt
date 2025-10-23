package com.example.demo.data.mapper

import com.example.demo.data.local.entity.CurrencyEntity
import com.example.demo.domain.model.CurrencyInfo

fun CurrencyEntity.toDomain(): CurrencyInfo {
    return CurrencyInfo(
        id = this.id,
        name = this.name,
        symbol = this.symbol,
        code = this.code
    )
}

fun CurrencyInfo.toEntity(): CurrencyEntity {
    return CurrencyEntity(
        id = this.id,
        name = this.name,
        symbol = this.symbol,
        code = this.code
    )
}

fun List<CurrencyEntity>.toDomain(): List<CurrencyInfo> {
    return this.map { it.toDomain() }
}

fun List<CurrencyInfo>.toEntity(): List<CurrencyEntity> {
    return this.map { it.toEntity() }
}
