package com.example.foody.use_cases

import com.example.foody.data.repositories.Repository
import com.example.foody.models.Recipe

class InsertRecentlyVisitedRecipe(
    private val repository: Repository
) {
    suspend operator fun invoke(recipe: Recipe) {
        repository.local.insertRecentlyVisitedRecipe(recipe)
    }
}