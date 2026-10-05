package com.example.socket_invest.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MarketDataDao {
    @Query("SELECT * FROM market_data ORDER BY symbol ASC")
    fun observeAllMarketData(): Flow<List<MarketDataEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateAll(items: List<MarketDataEntity>)

    @Query("DELETE FROM market_data")
    suspend fun clearAll()
}
