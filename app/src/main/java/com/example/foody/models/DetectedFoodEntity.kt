package com.example.foody.models


import com.google.gson.annotations.SerializedName

data class DetectedFoodEntity(
    @SerializedName("annotation")
    val name: String,
    @SerializedName("image")
    val imageUrl: String?,
    @SerializedName("tag")
    val tag: String // "dish" or "ingredient"
)

fun DetectedFoodEntity.toIngredient() =
    Ingredient(name, imageUrl.orEmpty())
