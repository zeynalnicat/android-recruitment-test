package com.example.socket_invest.data.injection

import android.content.Context
import androidx.room.Room
import com.example.socket_invest.data.local.AppDatabase
import com.example.socket_invest.data.local.MarketDataDao
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
@ComponentScan("com.example.socket_invest.data")
class DataModule {

    @Single
    fun provideAppDatabase(context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "market_data_db"
        ).fallbackToDestructiveMigration().build()
    }

    @Single
    fun provideMarketDataDao(database: AppDatabase): MarketDataDao {
        return database.marketDataDao()
    }

}
