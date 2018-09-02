package com.peterdang.androidcatchup.features.home.data

import com.peterdang.androidcatchup.features.home.models.FunctionModel
import retrofit2.Call
import retrofit2.http.GET

internal interface FunctionAPI {
    companion object {
        private const val VERSION = "v1.0su"
        private const val FUNCTIONS = "/7c68c68398c7"
    }

    @GET(VERSION + FUNCTIONS)
    fun functions(): Call<List<FunctionModel>>
}