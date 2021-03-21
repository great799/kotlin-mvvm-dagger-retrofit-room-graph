package com.app.stock.injection.component

import com.app.stock.MainActivity
import com.app.stock.injection.module.AppDatabaseModule
import com.app.stock.injection.module.AppModule
import com.app.stock.injection.module.NetworkModule
import com.app.stock.view_model.HistoryViewModel
import com.app.stock.view_model.ListOfStocksViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [AppModule::class, NetworkModule::class, AppDatabaseModule::class]
)
interface AppComponent {
    fun inject(mainActivity: MainActivity)
    fun inject(listOfStocksViewModel: ListOfStocksViewModel)
    fun inject(historyViewModel: HistoryViewModel)
}