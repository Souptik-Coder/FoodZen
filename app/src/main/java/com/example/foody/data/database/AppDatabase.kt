package com.example.foody.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.foody.data.database.dao.RecipeDao
import com.example.foody.data.database.entities.FavouritesEntity
import com.example.foody.data.database.entities.RecentlyVisitedEntity
import com.example.foody.data.database.typeconverters.TypeConverter

@Database(entities = [FavouritesEntity::class,RecentlyVisitedEntity::class], version = 1)
@TypeConverters(TypeConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun resultDao(): RecipeDao
}