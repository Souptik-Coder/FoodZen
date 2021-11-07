package com.example.foody.data.database.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.foody.models.Recipe

@Entity(tableName = "Favourites")
data class FavouritesEntity(
    @Embedded(prefix = "_") val recipe: Recipe,
    @PrimaryKey(autoGenerate = false) val id: Int = recipe.id
)

fun Recipe.toFavouriteEntity(): FavouritesEntity =
    FavouritesEntity(this)

fun FavouritesEntity.toRecipe(): Recipe =
    this.recipe