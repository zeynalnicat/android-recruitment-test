package com.example.socket_invest.domain.socket

import com.example.socket_invest.domain.model.ConnectionState
import com.example.socket_invest.domain.model.MarketData
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface SocketService {
    val connectionState: StateFlow<ConnectionState>
    val marketDataEvents: SharedFlow<List<MarketData>>
    fun connect()
    fun disconnect()
}
