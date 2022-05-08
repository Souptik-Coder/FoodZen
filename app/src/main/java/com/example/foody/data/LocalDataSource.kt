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

    fun getAllRecentlyVisitedRecipes(): Flow<List<Recipe>> {
        return db.resultDao().getAllRecentlyVisitedRecipes().map { recentEntityList ->
            recentEntityList.map { it.toRecipe() }
        }
    }

    suspend fun insertRecentlyVisitedRecipe(recipe: Recipe) {
        db.resultDao().insertRecentlyVisitedRecipe(recipe.toRecentEntity())
    }

    suspend fun deleteFavouriteRecipe(recipe: Recipe) {
        db.resultDao().deleteFromFavourite(recipe.toFavouriteEntity())
    }
}