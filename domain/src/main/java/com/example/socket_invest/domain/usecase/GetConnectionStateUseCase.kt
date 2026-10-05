package com.example.socket_invest.domain.usecase

import com.example.socket_invest.domain.model.ConnectionState
import com.example.socket_invest.domain.repository.MarketDataRepository
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory

@Factory
class GetConnectionStateUseCase(
    private val repository: MarketDataRepository
) {
    operator fun invoke(): Flow<ConnectionState> {
        return repository.getConnectionState()
    }
}
