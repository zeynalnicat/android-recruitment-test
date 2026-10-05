package org.koin.ksp.generated

import org.koin.core.module.Module
import org.koin.dsl.*


public val com_example_socket_invest_domain_injection_DomainModule : Module = module {
	factory() { com.example.socket_invest.domain.usecase.ConnectSocketUseCase(repository=get()) } 
	factory() { com.example.socket_invest.domain.usecase.DisconnectSocketUseCase(repository=get()) } 
	factory() { com.example.socket_invest.domain.usecase.GetConnectionStateUseCase(repository=get()) } 
	factory() { com.example.socket_invest.domain.usecase.GetMarketDataUseCase(repository=get()) } 
}
public val com.example.socket_invest.domain.injection.DomainModule.module : org.koin.core.module.Module get() = com_example_socket_invest_domain_injection_DomainModule