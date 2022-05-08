package com.example.foody.models


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class RecipeList(
    @SerializedName("results")
    val recipeList: List<Recipe>,
):Parcelable