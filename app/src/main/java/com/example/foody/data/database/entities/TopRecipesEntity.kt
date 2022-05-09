package com.example.foody.data.database.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.foody.models.Recipe

@Entity(tableName = "top_recipes")
data class TopRecipesEntity(
    @Embedded(prefix = "_") val recipe: Recipe,
    @PrimaryKey(autoGenerate = false)
    val id: Int = recipe.id
)

fun Recipe.toTopRecipesEntity(): TopRecipesEntity =
    TopRecipesEntity(this)

fun TopRecipesEntity.toRecipe(): Recipe =
    this.recipe