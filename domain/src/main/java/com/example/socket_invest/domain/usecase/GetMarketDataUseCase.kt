package com.example.socket_invest.domain.usecase

import com.example.socket_invest.domain.model.MarketData
import com.example.socket_invest.domain.repository.MarketDataRepository
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory

@Factory
class GetMarketDataUseCase(
    private val repository: MarketDataRepository
) {
    operator fun invoke(): Flow<List<MarketData>> {
        return repository.getMarketDataStream()
    }
}
