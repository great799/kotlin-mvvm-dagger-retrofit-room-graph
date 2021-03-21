package com.app.stock.injection.module

import com.app.stock.SmallCaseApp
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private val application: SmallCaseApp) {

    @Singleton
    @Provides
    internal fun providesApplication(): SmallCaseApp {
        return application
    }
}