package org.koin.ksp.generated

import org.koin.core.module.Module
import org.koin.dsl.*


public val com_example_socket_invest_data_injection_DataModule : Module = module {
	val moduleInstance = com.example.socket_invest.data.injection.DataModule()
	single() { moduleInstance.provideAppDatabase(context=get()) } bind(com.example.socket_invest.data.local.AppDatabase::class)
	single() { moduleInstance.provideMarketDataDao(database=get()) } bind(com.example.socket_invest.data.local.MarketDataDao::class)
	single() { com.example.socket_invest.data.remote.SocketServiceImpl() } bind(com.example.socket_invest.domain.socket.SocketService::class)
	single() { com.example.socket_invest.data.repository.MarketDataRepositoryImpl(socketService=get(),marketDataDao=get()) } bind(com.example.socket_invest.domain.repository.MarketDataRepository::class)
}
public val com.example.socket_invest.data.injection.DataModule.module : org.koin.core.module.Module get() = com_example_socket_invest_data_injection_DataModule