package com.example.foody.models


import com.google.gson.annotations.SerializedName

data class RecipeCard(
    @SerializedName("status")
    val status: String?,
    @SerializedName("time")
    val time: String?,
    @SerializedName("url")
    val url: String?
)