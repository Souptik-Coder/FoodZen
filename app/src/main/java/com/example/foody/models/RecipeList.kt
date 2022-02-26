package com.example.foody.models


import android.os.Parcelable
import com.example.foody.data.database.entities.FavouritesEntity
import com.example.foody.data.database.entities.RecentEntity
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class RecipeList(
    @SerializedName("results")
    val recipeList: List<Recipe>,
):Parcelable