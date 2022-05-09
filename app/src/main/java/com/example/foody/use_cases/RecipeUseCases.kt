package com.example.foody.use_cases

data class RecipeUseCases(
    val getRecipes: GetRecipes,
    val getRecipeById: GetRecipeById,
    val getAllRecentlyVisitedRecipes: GetAllRecentlyVisitedRecipes,
    val getFavouriteRecipes: GetFavouriteRecipes,
    val insertFavouriteRecipes: InsertFavouriteRecipes,
    val deleteFavouriteRecipes: DeleteFavouriteRecipes,
    val insertRecentlyVisitedRecipe: InsertRecentlyVisitedRecipe,
    val getRecipesByIngredient: GetRecipesByIngredient,
    val detectFoodInText: DetectFoodInText,
    val getIngredientSuggestion: GetIngredientSuggestion,
    val getRecipeSuggestion: GetRecipesSuggestion,
    val getAnalyzedInstruction: GetAnalyzedInstruction,
    val insertAllTopRecipes: InsertAllTopRecipes,
    val getAllTopRecipes: GetAllTopRecipes
)