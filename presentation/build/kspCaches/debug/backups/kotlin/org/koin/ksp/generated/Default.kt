package org.koin.ksp.generated

import org.koin.core.KoinApplication
import org.koin.core.module.Module
import org.koin.dsl.*
import org.koin.androidx.viewmodel.dsl.viewModel
public fun KoinApplication.defaultModule(): KoinApplication = modules(defaultModule)
public val defaultModule : Module = module {
	viewModel() { com.example.socket_invest.presentation.MarketViewModel(getMarketDataUseCase=get(),getConnectionStateUseCase=get(),connectSocketUseCase=get(),disconnectSocketUseCase=get()) } 
}