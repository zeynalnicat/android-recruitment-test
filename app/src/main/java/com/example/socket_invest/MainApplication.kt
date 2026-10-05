package com.example.socket_invest

import android.app.Application
import com.example.socket_invest.data.injection.DataModule
import com.example.socket_invest.domain.injection.DomainModule
import com.example.socket_invest.presentation.injection.PresentationModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.ksp.generated.module

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MainApplication)
            modules(
                DomainModule().module,
                DataModule().module,
                PresentationModule().module
            )
        }
    }
}
