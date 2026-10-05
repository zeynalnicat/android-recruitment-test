package com.example.socket_invest.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.socket_invest.domain.model.ConnectionState
import com.example.socket_invest.domain.usecase.ConnectSocketUseCase
import com.example.socket_invest.domain.usecase.DisconnectSocketUseCase
import com.example.socket_invest.domain.usecase.GetConnectionStateUseCase
import com.example.socket_invest.domain.usecase.GetMarketDataUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class MarketViewModel(
    private val getMarketDataUseCase: GetMarketDataUseCase,
    private val getConnectionStateUseCase: GetConnectionStateUseCase,
    private val connectSocketUseCase: ConnectSocketUseCase,
    private val disconnectSocketUseCase: DisconnectSocketUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(MarketContract.MarketUiState())
    val uiState: StateFlow<MarketContract.MarketUiState> = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<MarketContract.MarketUiEffect>()
    val uiEffect: SharedFlow<MarketContract.MarketUiEffect> = _uiEffect.asSharedFlow()

    init {
        observeMarketData()
        observeConnectionState()
        processIntent(MarketContract.MarketIntent.Connect)
    }

    fun processIntent(intent: MarketContract.MarketIntent) {
        when (intent) {
            is MarketContract.MarketIntent.Connect -> {
                connectSocketUseCase()
            }
            is MarketContract.MarketIntent.Disconnect -> {
                disconnectSocketUseCase()
            }
            is MarketContract.MarketIntent.ToggleConnection -> {
                if (_uiState.value.connectionState == ConnectionState.CONNECTED) {
                    disconnectSocketUseCase()
                } else {
                    connectSocketUseCase()
                }
            }
        }
    }

    private fun observeMarketData() {
        viewModelScope.launch {
            getMarketDataUseCase().collect { dataList ->
                _uiState.update { it.copy(marketList = dataList) }
            }
        }
    }

    private fun observeConnectionState() {
        viewModelScope.launch {
            getConnectionStateUseCase().collect { state ->
                _uiState.update { it.copy(connectionState = state) }
            }
        }
    }

    override fun onCleared() {
        disconnectSocketUseCase()
    }
}