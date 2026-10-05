package com.example.socket_invest.presentation

import com.example.socket_invest.domain.model.ConnectionState
import com.example.socket_invest.domain.model.MarketData

sealed interface MarketContract {

    data class MarketUiState(
        val connectionState: ConnectionState = ConnectionState.DISCONNECTED,
        val marketList: List<MarketData> = emptyList(),
        val isLoading: Boolean = false,
        val errorMessage: String? = null
    )

    sealed interface MarketUiEffect {
        data class ShowMessage(val message: String) : MarketUiEffect
    }

    sealed interface MarketIntent {
        object Connect : MarketIntent
        object Disconnect : MarketIntent
        object ToggleConnection : MarketIntent
    }



}