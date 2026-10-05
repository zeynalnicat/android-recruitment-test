package com.example.socket_invest.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "market_data")
data class MarketDataEntity(
    @PrimaryKey val id: String,
    val symbol: String,
    val price: Double,
    val change: Double,
    val timestamp: Long
)

