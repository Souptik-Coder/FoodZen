package com.example.foody.data.database.dao

import androidx.room.*
import com.example.foody.data.database.entities.FavouritesEntity
import com.example.foody.data.database.entities.RecentEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeDao {
    @Query("SELECT * FROM FAVOURITES")
    fun getAllFavouriteRecipes(): Flow<List<FavouritesEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavouriteRecipes(favouritesEntity: FavouritesEntity)

    @Delete
    suspend fun deleteFromFavourite(favouritesEntity: FavouritesEntity)

    @Query("SELECT * FROM RECENT ORDER BY _aggregateLikes DESC")
    fun getAllRecentRecipes(): Flow<List<RecentEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllRecentRecipes(recentEntity: List<RecentEntity>)

    @Query("DELETE FROM RECENT")
    suspend fun deleteAllRecentRecipes()
}