package com.peterdang.androidcatchup.features.home.data

import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FunctionService @Inject constructor(retrofit: Retrofit) : FunctionAPI {
    private val funtioncApi by lazy { retrofit.create(FunctionAPI::class.java) }

    override fun functions() = funtioncApi.functions()
}