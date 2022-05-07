package com.example.foody.models


import com.google.gson.annotations.SerializedName

data class RecipeSuggestionItem(
    @SerializedName("id")
    val id: Int,
    @SerializedName("imageType")
    val imageType: String,
    @SerializedName("title")
    val title: String
)