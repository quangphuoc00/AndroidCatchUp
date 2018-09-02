package com.peterdang.androidcatchup.core.di

import com.peterdang.androidcatchup.MyApplication
import com.peterdang.androidcatchup.core.di.viewmodel.ViewModelModule
import com.peterdang.androidcatchup.features.MainActivity
import com.peterdang.androidcatchup.features.blur.BlurFragment
import com.peterdang.androidcatchup.features.home.HomeFragment
import com.peterdang.androidcatchup.features.room.RoomFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, ViewModelModule::class, RepositoryModule::class])
interface ApplicationComponent {
    fun inject(application: MyApplication)
    fun inject(homeActivity: MainActivity)

    fun inject(roomFragment: RoomFragment)
    fun inject(functionFragment: HomeFragment)
    fun inject(blurFragment: BlurFragment)
}