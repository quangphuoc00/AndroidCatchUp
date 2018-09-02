package com.peterdang.androidcatchup.core.di

import com.peterdang.androidcatchup.features.home.data.FunctionRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModule {
    @Provides
    @Singleton
    fun provideFunctionRepository(dataSource: FunctionRepository.Network): FunctionRepository = dataSource
}