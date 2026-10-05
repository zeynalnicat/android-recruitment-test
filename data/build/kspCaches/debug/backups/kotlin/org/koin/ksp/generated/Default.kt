package org.koin.ksp.generated

import org.koin.core.KoinApplication
import org.koin.core.module.Module
import org.koin.dsl.*

public fun KoinApplication.defaultModule(): KoinApplication = modules(defaultModule)
public val defaultModule : Module = module {
	single() { com.example.socket_invest.data.repository.MarketDataRepositoryImpl(socketService=get(),marketDataDao=get()) } bind(com.example.socket_invest.domain.repository.MarketDataRepository::class)
}