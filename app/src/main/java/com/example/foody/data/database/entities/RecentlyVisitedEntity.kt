package com.example.foody.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.foody.models.Recipe

@Entity(tableName = "RecentlyVisited")
data class RecentlyVisitedEntity(
    @Embedded(prefix = "_") val recipe: Recipe,
    @ColumnInfo(name = "timeInMillis") val timeInMillis: Long = System.currentTimeMillis(),
    @PrimaryKey(autoGenerate = false)
    val id: Int = recipe.id
)

fun Recipe.toRecentEntity(): RecentlyVisitedEntity =
    RecentlyVisitedEntity(this)

fun RecentlyVisitedEntity.toRecipe(): Recipe =
    this.recipe