package com.example.foody.use_cases

import com.example.foody.data.repositories.Repository
import com.example.foody.models.Recipe

class InsertFavouriteRecipes(
    private val repository: Repository
) {
    suspend operator fun invoke(recipe: Recipe) {
        repository.local.insertFavouriteRecipes(recipe)
    }
}