package com.example.foody.util

object Constants {
    const val BASE_URL = "https://api.spoonacular.com/"
    const val BASE_INGREDIENT_IMAGE_URL = "https://spoonacular.com/cdn/ingredients_100x100/"
    private const val BASE_RECIPE_IMAGE_URL = "https://spoonacular.com/recipeImages/"
    const val API_KEY = "b55947b3254449a2a6e735742300a0b2" //"9db6bc7d4cd0441fa7aa2054d853f21e"

    //API Query Keys
    const val QUERY_NUMBER = "number"
    const val QUERY_API_KEY = "apiKey"
    const val QUERY_TYPE = "type"
    const val QUERY_DIET = "diet"
    const val QUERY_CUISINE = "cuisine"
    const val QUERY_ADD_RECIPE_INFORMATION = "addRecipeInformation"
    const val QUERY_FILL_INGREDIENTS = "fillIngredients"
    const val QUERY_INTOLERANCES = "intolerances"
    const val QUERY_SORT = "sort"
    const val QUERY_SORT_DIRECTIONS = "sortDirection"

    //Bottom sheet and Preferences
    const val NO_FILTER = "" //empty filter value
    const val DEFAULT_RECIPES_NUMBER = "50"
    const val DEFAULT_MEAL_TYPE = NO_FILTER
    const val DEFAULT_DIET = NO_FILTER
    const val DEFAULT_CUISINE = NO_FILTER
    const val DEFAULT_INTOLERANCE = NO_FILTER
    const val DEFAULT_SORT = "popularity"
    const val DEFAULT_SORT_DIRECTIONS = "desc"

    const val PREFERENCES_NAME = "recipeFilterPreference"
    const val PREFERENCES_MEAL_TYPE = "mealType"
    const val PREFERENCES_MEAL_TYPE_ID = "mealTypeId"
    const val PREFERENCES_DIET_TYPE = "dietType"
    const val PREFERENCES_DIET_TYPE_ID = "dietTypeId"

    fun buildRecipeImageUrl(id: Int, imageType: String) =
        "$BASE_RECIPE_IMAGE_URL$id-240x150.$imageType"
}