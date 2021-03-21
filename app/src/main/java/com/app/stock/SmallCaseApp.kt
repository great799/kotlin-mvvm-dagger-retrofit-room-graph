package com.app.stock

import android.app.Application
import com.app.stock.injection.component.AppComponent
import com.app.stock.injection.component.DaggerAppComponent
import com.app.stock.injection.module.AppDatabaseModule
import com.app.stock.injection.module.AppModule
import dagger.android.AndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

class SmallCaseApp : Application(), HasAndroidInjector {

    @Inject
    lateinit var androidInjector: AndroidInjector<Any>

    override fun androidInjector(): AndroidInjector<Any> {
        return androidInjector
    }

    companion object {
        private var appComponent: AppComponent? = null
        private var instance: SmallCaseApp? = null

        fun getAppComponent(): AppComponent {
            return appComponent!!
        }

        /**
         * Using this instance, we can access any method from this SmallCaseApp class.
         */
        fun getInstance(): SmallCaseApp {
            return instance ?: SmallCaseApp()
        }
    }


    override fun onCreate() {
        super.onCreate()
        instance = this
        appComponent = DaggerAppComponent.builder().appDatabaseModule(AppDatabaseModule(this)).appModule(AppModule(this)).build()
    }
}