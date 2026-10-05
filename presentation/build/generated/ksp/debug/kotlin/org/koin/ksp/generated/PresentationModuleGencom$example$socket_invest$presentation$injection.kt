package org.koin.ksp.generated

import org.koin.core.module.Module
import org.koin.dsl.*
import org.koin.androidx.viewmodel.dsl.viewModel

public val com_example_socket_invest_presentation_injection_PresentationModule : Module = module {
	viewModel() { com.example.socket_invest.presentation.MarketViewModel(getMarketDataUseCase=get(),getConnectionStateUseCase=get(),connectSocketUseCase=get(),disconnectSocketUseCase=get()) } 
}
public val com.example.socket_invest.presentation.injection.PresentationModule.module : org.koin.core.module.Module get() = com_example_socket_invest_presentation_injection_PresentationModule