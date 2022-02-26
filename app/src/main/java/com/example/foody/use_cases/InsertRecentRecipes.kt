package com.example.foody.use_cases

import com.example.foody.data.repositories.Repository
import com.example.foody.models.Recipe

class InsertRecentRecipes(
    private val repository: Repository
) {
    suspend operator fun invoke(recipes:List<Recipe>){
        repository.local.insertAllRecentRecipes(recipes)
    }
}