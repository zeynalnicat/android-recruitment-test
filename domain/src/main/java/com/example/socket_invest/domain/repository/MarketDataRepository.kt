package com.example.socket_invest.domain.repository

import com.example.socket_invest.domain.model.ConnectionState
import com.example.socket_invest.domain.model.MarketData
import kotlinx.coroutines.flow.Flow

interface MarketDataRepository {
    fun getMarketDataStream(): Flow<List<MarketData>>
    fun getConnectionState(): Flow<ConnectionState>
    fun connect()
    fun disconnect()
}
