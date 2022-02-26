package com.example.foody.data

import com.example.foody.data.database.AppDatabase
import com.example.foody.data.database.entities.toFavouriteEntity
import com.example.foody.data.database.entities.toRecentEntity
import com.example.foody.data.database.entities.toRecipe
import com.example.foody.models.Recipe
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LocalDataSource @Inject constructor(private val db: AppDatabase) {
    fun getAllFavouriteRecipes(): Flow<List<Recipe>> {
        return db.resultDao().getAllFavouriteRecipes().map { list -> list.map { it.toRecipe() } }
    }

    suspend fun insertFavouriteRecipes(recipe: Recipe) {
        db.resultDao().insertFavouriteRecipes(recipe.toFavouriteEntity())
    }

    fun getAllRecentRecipes(): Flow<List<Recipe>> {
        return db.resultDao().getAllRecentRecipes().map { recentEntityList ->
            recentEntityList.map { it.toRecipe() }
        }
    }

    suspend fun insertAllRecentRecipes(recipes: List<Recipe>) {
        db.resultDao().deleteAllRecentRecipes()
        db.resultDao().insertAllRecentRecipes(recipes.map { it.toRecentEntity() })
    }

    suspend fun deleteFavouriteRecipe(recipe: Recipe) {
        db.resultDao().deleteFromFavourite(recipe.toFavouriteEntity())
    }
}