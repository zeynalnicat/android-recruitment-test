package com.example.socket_invest.data.repository

import com.example.socket_invest.data.local.MarketDataDao
import com.example.socket_invest.data.mapper.toDomainModel
import com.example.socket_invest.data.mapper.toEntity
import com.example.socket_invest.domain.model.ConnectionState
import com.example.socket_invest.domain.model.MarketData
import com.example.socket_invest.domain.repository.MarketDataRepository
import com.example.socket_invest.domain.socket.SocketService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.koin.core.annotation.Single

@Single
class MarketDataRepositoryImpl(
    private val socketService: SocketService,
    private val marketDataDao: MarketDataDao
) : MarketDataRepository {

    private val scope = CoroutineScope(Dispatchers.IO)

    init {
        scope.launch {
            socketService.marketDataEvents.collect { list ->
                if (list.isNotEmpty()) {
                    marketDataDao.insertOrUpdateAll(list.map { it.toEntity() })
                }
            }
        }
    }

    override fun getMarketDataStream(): Flow<List<MarketData>> {
        return marketDataDao.observeAllMarketData().map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    override fun getConnectionState(): Flow<ConnectionState> {
        return socketService.connectionState
    }

    override fun connect() {
        socketService.connect()
    }

    override fun disconnect() {
        socketService.disconnect()
    }
}
