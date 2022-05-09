package com.example.foody.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.foody.data.database.dao.RecipeDao
import com.example.foody.data.database.entities.FavouritesEntity
import com.example.foody.data.database.entities.RecentlyVisitedEntity
import com.example.foody.data.database.entities.TopRecipesEntity
import com.example.foody.data.database.typeconverters.TypeConverter
import com.example.foody.models.Recipe
import com.example.foody.models.RecipeList
import com.google.gson.Gson

@Database(entities = [FavouritesEntity::class,RecentlyVisitedEntity::class,TopRecipesEntity::class], version = 1)
@TypeConverters(TypeConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun resultDao(): RecipeDao
}