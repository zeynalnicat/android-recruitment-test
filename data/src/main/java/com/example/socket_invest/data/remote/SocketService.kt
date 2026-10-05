package com.example.socket_invest.data.remote

import android.os.Build
import android.util.Log
import com.example.socket_invest.domain.model.ConnectionState
import com.example.socket_invest.domain.model.MarketData
import com.example.socket_invest.domain.socket.SocketService
import io.socket.client.IO
import io.socket.client.Socket
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import org.koin.core.annotation.Single
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.Locale
import java.util.TimeZone
import kotlin.math.abs


@Single
class SocketServiceImpl : SocketService {
    private val socketUrl: String = "https://q.investaz.az"

    private val _connectionState = MutableStateFlow(ConnectionState.DISCONNECTED)
    override val connectionState: StateFlow<ConnectionState> = _connectionState.asStateFlow()

    private val _marketDataEvents = MutableSharedFlow<List<MarketData>>()
    override val marketDataEvents: SharedFlow<List<MarketData>> = _marketDataEvents.asSharedFlow()

    private var socket: Socket? = null
    private val scope = CoroutineScope(Dispatchers.IO)

    override fun connect() {
        if (socket?.connected() == true) return

        try {
            _connectionState.value = ConnectionState.CONNECTING

            val opts = IO.Options().apply {
                path = "/live"
                forceNew = true
                reconnection = true
            }

            socket = IO.socket(socketUrl, opts).apply {
                on(Socket.EVENT_CONNECT) {
                    _connectionState.value = ConnectionState.CONNECTED
                }

                on(Socket.EVENT_DISCONNECT) {
                    _connectionState.value = ConnectionState.DISCONNECTED
                }

                on(Socket.EVENT_CONNECT_ERROR) { args ->
                    _connectionState.value = ConnectionState.ERROR

                    args.forEach { error ->
                        Log.e("socket", "CONNECT ERROR: $error")
                    }
                }

                on("message") { args ->
                    if (args.isNotEmpty()) {
                        args.forEach { arg ->
                            val parsedData = parseMarketData(arg)
                            if (parsedData.isNotEmpty()) {
                                scope.launch {
                                    _marketDataEvents.emit(parsedData)
                                }
                            }
                        }
                    }
                }

                connect()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("socket", e.message.toString())
            _connectionState.value = ConnectionState.ERROR
        }
    }

    override fun disconnect() {
        socket?.disconnect()
        socket?.off()
        socket = null
        _connectionState.value = ConnectionState.DISCONNECTED
    }

    private fun parseMarketData(data: Any?): List<MarketData> {
        val result = mutableListOf<MarketData>()
        try {
            when (data) {
                is JSONObject -> {
                    parseJsonObject(data, result)
                }
                is JSONArray -> {
                    parseJsonArray(data, result)
                }
                is String -> {
                    val trimmed = data.trim()
                    if (trimmed.startsWith("{")) {
                        parseJsonObject(JSONObject(trimmed), result)
                    } else if (trimmed.startsWith("[")) {
                        parseJsonArray(JSONArray(trimmed), result)
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("socket", "Error parsing market data: ${e.message}")
        }
        return result
    }

    private fun parseJsonObject(jsonObj: JSONObject, result: MutableList<MarketData>) {
        val resultArray = jsonObj.optJSONArray("result") ?: jsonObj.optJSONArray("data")
        if (resultArray != null) {
            parseJsonArray(resultArray, result)
        } else {
            jsonObj.toMarketData()?.let { result.add(it) }
        }
    }

    private fun parseJsonArray(jsonArray: JSONArray, result: MutableList<MarketData>) {
        for (i in 0 until jsonArray.length()) {
            val jsonObj = jsonArray.optJSONObject(i)
            jsonObj?.toMarketData()?.let { result.add(it) }
        }
    }

    private fun JSONObject.toMarketData(): MarketData? {
        return try {
            val symbol = when {
                has("1") -> optString("1")
                has("symbol") -> optString("symbol")
                has("code") -> optString("code")
                else -> optString("0", "UNKNOWN")
            }

            val id = if (has("id")) optString("id") else symbol

            val rawPriceStr = optString("2", optString("price", optString("last", "0.0")))
            val price = rawPriceStr.toDoubleOrNull() ?: optDouble("2", optDouble("price", optDouble("last", 0.0)))

            val rawVal3Str = optString("3", optString("diff", optString("change", "0.0")))
            val val3 = rawVal3Str.toDoubleOrNull() ?: optDouble("3", optDouble("diff", optDouble("change", 0.0)))

            var change = if (val3 != 0.0 && price != 0.0 && abs(val3) > abs(price) * 0.2) {
                price - val3
            } else {
                val3
            }

            val direction = optString("0", "").lowercase()
            if (direction == "down" && change > 0) {
                change = -change
            } else if (direction == "up" && change < 0) {
                change = abs(change)
            }

            val rawTimestamp = opt("7") ?: opt("timestamp") ?: opt("time")
            val timestamp = parseTimestamp(rawTimestamp)

            MarketData(
                id = id,
                symbol = symbol,
                price = price,
                change = change,
                timestamp = timestamp
            )
        } catch (_: Exception) {
            null
        }
    }

    private fun parseTimestamp(raw: Any?): Long {
        if (raw == null) return System.currentTimeMillis()
        if (raw is Number) return raw.toLong()
        val str = raw.toString().trim()
        if (str.isEmpty()) return System.currentTimeMillis()

        str.toLongOrNull()?.let { return it }

        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Instant.parse(str).toEpochMilli()
            } else {
                val cleanStr = str.replace(Regex("\\.\\d+"), "")
                val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US).apply {
                    timeZone = TimeZone.getTimeZone("UTC")
                }
                sdf.parse(cleanStr)?.time ?: System.currentTimeMillis()
            }
        } catch (e: Exception) {
            try {
                val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US).apply {
                    timeZone = TimeZone.getTimeZone("UTC")
                }
                sdf.parse(str)?.time ?: System.currentTimeMillis()
            } catch (_: Exception) {
                System.currentTimeMillis()
            }
        }
    }
}
