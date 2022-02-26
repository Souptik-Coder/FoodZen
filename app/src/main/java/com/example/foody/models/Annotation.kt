package com.example.foody.models


import com.google.gson.annotations.SerializedName

data class Annotation(
    @SerializedName("annotation")
    val name: String,
    @SerializedName("image")
    val image: String?,
    @SerializedName("tag")
    val tag: String
)

fun Annotation.toIngredient() =
    Ingredient(name, image.orEmpty())