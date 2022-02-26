package com.example.foody.use_cases

data class RecipeUseCases(
    val getRecipes: GetRecipes,
    val getRecipeById: GetRecipeById,
    val getRecentRecipes: GetRecentRecipes,
    val getFavouriteRecipes: GetFavouriteRecipes,
    val insertFavouriteRecipes: InsertFavouriteRecipes,
    val deleteFavouriteRecipes: DeleteFavouriteRecipes,
    val insertRecentRecipes: InsertRecentRecipes,
    val getRecipesByIngredient: GetRecipesByIngredient,
    val detectFoodInText: DetectFoodInText,
    val getIngredientSuggestion: GetIngredientSuggestion
)