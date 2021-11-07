package com.example.foody.di

import android.content.Context
import androidx.room.Room
import com.example.foody.data.repositories.DataStoreRepository
import com.example.foody.data.FoodRecipesApi
import com.example.foody.data.database.AppDatabase
import com.example.foody.util.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideApiServices(retrofit: Retrofit): FoodRecipesApi =
        retrofit.create(FoodRecipesApi::class.java)

    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): DataStoreRepository =
        DataStoreRepository(context)

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java, "Recipes Database"
        ).build()
}