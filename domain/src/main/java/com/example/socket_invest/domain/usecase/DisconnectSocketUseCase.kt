package com.example.socket_invest.domain.usecase

import com.example.socket_invest.domain.repository.MarketDataRepository
import org.koin.core.annotation.Factory

@Factory
class DisconnectSocketUseCase(
    private val repository: MarketDataRepository
) {
    operator fun invoke() {
        repository.disconnect()
    }
}
