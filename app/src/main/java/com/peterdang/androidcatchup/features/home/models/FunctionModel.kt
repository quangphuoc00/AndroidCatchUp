package com.peterdang.androidcatchup.features.home.models

import com.google.gson.annotations.SerializedName

data class FunctionModel(
        @SerializedName("id") val id: Int,
        @SerializedName("function_name") val functionName: String,
        @SerializedName("tech_name") val techName: String,
        @SerializedName("function_code") val functionCode: String
)