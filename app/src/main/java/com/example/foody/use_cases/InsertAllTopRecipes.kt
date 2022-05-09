package com.example.foody.use_cases

import com.example.foody.data.repositories.Repository
import com.example.foody.models.Recipe

class InsertAllTopRecipes(private val repository: Repository) {
    suspend operator fun invoke(topRecipes: List<Recipe>) {
        repository.local.insertAllTopRecipes(topRecipes)
    }
}