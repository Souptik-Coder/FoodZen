package com.example.foody.use_cases

import com.example.foody.data.repositories.Repository
import com.example.foody.models.Recipe
import kotlinx.coroutines.flow.Flow

class GetFavouriteRecipes(
    private val repository: Repository
) {
    operator fun invoke(): Flow<List<Recipe>> {
        return repository.local.getAllFavouriteRecipes()
    }
}