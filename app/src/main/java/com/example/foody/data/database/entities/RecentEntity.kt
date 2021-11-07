package com.example.foody.data.database.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.foody.models.Recipe

@Entity(tableName = "Recent")
data class RecentEntity(
    @Embedded(prefix = "_") val recipe: Recipe,
    @PrimaryKey(autoGenerate = false) val id: Int = recipe.id
)

fun Recipe.toRecentEntity(): RecentEntity =
    RecentEntity(this)

fun RecentEntity.toRecipe(): Recipe =
    this.recipe