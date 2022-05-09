package com.example.foody.data.database.dao

import androidx.room.*
import com.example.foody.data.database.entities.FavouritesEntity
import com.example.foody.data.database.entities.RecentlyVisitedEntity
import com.example.foody.data.database.entities.TopRecipesEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeDao {
    @Query("SELECT * FROM FAVOURITES")
    fun getAllFavouriteRecipes(): Flow<List<FavouritesEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavouriteRecipes(favouritesEntity: FavouritesEntity)

    @Delete
    suspend fun deleteFromFavourite(favouritesEntity: FavouritesEntity)

    @Query("SELECT * FROM recently_visited ORDER BY timeInMillis DESC")
    fun getAllRecentlyVisitedRecipes(): Flow<List<RecentlyVisitedEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecentlyVisitedRecipe(recentEntity: RecentlyVisitedEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllTopRecipes(topRecipesEntity: List<TopRecipesEntity>)

    @Query("SELECT * FROM top_recipes ORDER BY _aggregateLikes DESC")
    fun getAllTopRecipes(): Flow<List<TopRecipesEntity>>

    @Query("DELETE FROM top_recipes")
    suspend fun deleteAllTopRecipes()
}