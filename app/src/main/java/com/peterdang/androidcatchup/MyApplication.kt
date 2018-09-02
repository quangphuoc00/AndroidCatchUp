package com.peterdang.androidcatchup

import android.app.Application
import com.peterdang.androidcatchup.core.di.AppModule
import com.peterdang.androidcatchup.core.di.ApplicationComponent
import com.peterdang.androidcatchup.core.di.DaggerApplicationComponent
import com.squareup.leakcanary.LeakCanary

class MyApplication : Application() {

    val appComponent: ApplicationComponent by lazy(mode = LazyThreadSafetyMode.NONE) {
        DaggerApplicationComponent
                .builder()
                .appModule(AppModule(this))
                .build()
    }

    override fun onCreate() {
        super.onCreate()
        this.injectMembers()
        this.initializeLeakDetection()
    }

    private fun injectMembers() = appComponent.inject(this)

    private fun initializeLeakDetection() {
        if (BuildConfig.DEBUG) LeakCanary.install(this)
    }
}