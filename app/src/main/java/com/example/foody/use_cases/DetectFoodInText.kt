package com.example.foody.use_cases

import com.example.foody.R
import com.example.foody.data.repositories.Repository
import com.example.foody.models.Ingredient
import com.example.foody.models.toIngredient
import com.example.foody.util.NetworkResults
import java.io.IOException

class DetectFoodInText(
    private val repository: Repository
) {
    suspend operator fun invoke(text: String): NetworkResults<List<Ingredient>> {
        return try {
            val response = repository.remote.detectFoodInText(text)
            if (response.isSuccessful) {
                val ingredientsAnnotation = response.body()!!.annotations
                val ingredients = ArrayList<Ingredient>()

                ingredientsAnnotation.forEach {
                    if (it.tag == "ingredient")
                        ingredients.add(it.toIngredient())
                }
                NetworkResults.Success(ingredients)
            } else {
                NetworkResults.Error(R.string.unknown_error)
            }
        } catch (e: IOException) {
            NetworkResults.Error(R.string.internet_error)
        } catch (e: Exception) {
            NetworkResults.Error(R.string.unknown_error)
        }
    }
}