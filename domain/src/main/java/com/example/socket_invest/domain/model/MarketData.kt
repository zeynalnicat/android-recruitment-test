package com.example.socket_invest.domain.model

data class MarketData(
    val id: String,
    val symbol: String,
    val price: Double,
    val change: Double,
    val timestamp: Long
)
