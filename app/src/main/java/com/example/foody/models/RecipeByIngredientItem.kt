package com.example.foody.models


import com.google.gson.annotations.SerializedName

data class RecipeByIngredientItem(
    @SerializedName("id")
    val id: Int,
    @SerializedName("image")
    val image: String,
    @SerializedName("imageType")
    val imageType: String,
    @SerializedName("likes")
    val likes: Int,
    @SerializedName("missedIngredientCount")
    val missedIngredientCount: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("usedIngredientCount")
    val usedIngredientCount: Int
)

fun RecipeByIngredientItem.toRecipeListItem() =
    RecipeListItem(id, image, title)