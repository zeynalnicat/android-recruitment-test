package com.example.socket_invest.data.mapper

import com.example.socket_invest.data.local.MarketDataEntity
import com.example.socket_invest.domain.model.MarketData

fun MarketDataEntity.toDomainModel(): MarketData {
    return MarketData(
        id = id,
        symbol = symbol,
        price = price,
        change = change,
        timestamp = timestamp
    )
}

fun MarketData.toEntity(): MarketDataEntity {
    return MarketDataEntity(
        id = id,
        symbol = symbol,
        price = price,
        change = change,
        timestamp = timestamp
    )
}
