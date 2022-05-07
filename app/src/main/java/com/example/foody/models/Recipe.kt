package com.example.foody.models


import android.os.Parcelable
import androidx.room.ColumnInfo
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Recipe(
    @SerializedName("id")
    val id: Int,
    @ColumnInfo(name = "aggregateLikes") @SerializedName("aggregateLikes")
    val aggregateLikes: Int,
    @ColumnInfo(name = "dairyFree") @SerializedName("dairyFree")
    val dairyFree: Boolean,
    @ColumnInfo(name = "extendedIngredients") @SerializedName("extendedIngredients")
    val extendedIngredients: ArrayList<ExtendedIngredient>,
    @ColumnInfo(name = "glutenFree") @SerializedName("glutenFree")
    val glutenFree: Boolean,
    @ColumnInfo(name = "image") @SerializedName("image")
    val image: String,
    @ColumnInfo(name = "likes") @SerializedName("likes")
    val likes: Int,
    @ColumnInfo(name = "readyInMinutes") @SerializedName("readyInMinutes")
    val readyInMinutes: Int,
    @ColumnInfo(name = "sourceUrl") @SerializedName("sourceUrl")
    val sourceUrl: String,
    @ColumnInfo(name = "summary") @SerializedName("summary")
    val summary: String,
    @ColumnInfo(name = "title") @SerializedName("title")
    val title: String,
    @ColumnInfo(name = "vegan") @SerializedName("vegan")
    val vegan: Boolean,
    @ColumnInfo(name = "vegetarian") @SerializedName("vegetarian")
    val vegetarian: Boolean,
    @ColumnInfo(name = "veryHealthy") @SerializedName("veryHealthy")
    val veryHealthy: Boolean,
    @ColumnInfo(name = "veryPopular") @SerializedName("veryPopular")
    val veryPopular: Boolean,
    @ColumnInfo(name = "analyzedInstructions")
    val analyzedInstruction: AnalyzedInstructionItem? = null
) : Parcelable