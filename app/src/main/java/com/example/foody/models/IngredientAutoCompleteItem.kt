package com.example.foody.models


import com.example.foody.util.Constants
import com.google.gson.annotations.SerializedName

data class IngredientAutoCompleteItem(
    @SerializedName("id")
    val id: Int,
    @SerializedName("image")
    val image: String,
    @SerializedName("name")
    val name: String,
)

fun IngredientAutoCompleteItem.toIngredient() =
    Ingredient(name, Constants.BASE_INGREDIENT_IMAGE_URL + image)