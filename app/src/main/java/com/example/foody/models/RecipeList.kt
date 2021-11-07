package com.example.foody.models


import com.example.foody.data.database.entities.FavouritesEntity
import com.example.foody.data.database.entities.RecentEntity
import com.google.gson.annotations.SerializedName

data class RecipeList(
    @SerializedName("results")
    val recipeList: List<Recipe>,
)