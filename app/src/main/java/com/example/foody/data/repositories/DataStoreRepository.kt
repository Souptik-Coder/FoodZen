package com.example.foody.data.repositories

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.example.foody.util.Constants.DEFAULT_CUISINE
import com.example.foody.util.Constants.DEFAULT_DIET
import com.example.foody.util.Constants.DEFAULT_INTOLERANCE
import com.example.foody.util.Constants.DEFAULT_MEAL_TYPE
import com.example.foody.util.Constants.PREFERENCES_DIET_TYPE
import com.example.foody.util.Constants.PREFERENCES_DIET_TYPE_ID
import com.example.foody.util.Constants.PREFERENCES_MEAL_TYPE
import com.example.foody.util.Constants.PREFERENCES_MEAL_TYPE_ID
import com.example.foody.util.Constants.PREFERENCES_NAME
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

@ViewModelScoped
class DataStoreRepository @Inject constructor(@ApplicationContext private val context: Context) {
    private val TAG = "DataStoreRepository"

    private val Context.dataStore by preferencesDataStore(PREFERENCES_NAME)

    private object PreferenceKeys {
        val selectedMealType = stringPreferencesKey(PREFERENCES_MEAL_TYPE)
        val selectedMealTypeId = intPreferencesKey(PREFERENCES_MEAL_TYPE_ID)
        val selectedDietType = stringPreferencesKey(PREFERENCES_DIET_TYPE)
        val selectedDietTypeId = intPreferencesKey(PREFERENCES_DIET_TYPE_ID)
        val cuisineType = stringPreferencesKey("cuisineType")
        val cuisineTypeId = intPreferencesKey("cuisineTypeId")
        val intoleranceType = stringPreferencesKey("intoleranceType")
        val intoleranceTypeId = intPreferencesKey("intoleranceTypeId")
    }

    private val dataStore: DataStore<Preferences> = context.dataStore

    suspend fun saveRecipeFilterParameters(
        recipeFilterParameters: RecipeFilterParameters
    ) {
        dataStore.edit { preferences ->
            preferences[PreferenceKeys.selectedMealType] = recipeFilterParameters.selectedMealType
            preferences[PreferenceKeys.selectedMealTypeId] =
                recipeFilterParameters.selectedMealTypeId
            preferences[PreferenceKeys.selectedDietType] = recipeFilterParameters.selectedDietType
            preferences[PreferenceKeys.selectedDietTypeId] =
                recipeFilterParameters.selectedDietTypeId
            preferences[PreferenceKeys.cuisineType] = recipeFilterParameters.selectedCuisineType
            preferences[PreferenceKeys.cuisineTypeId] = recipeFilterParameters.selectedCuisineTypeId
            preferences[PreferenceKeys.intoleranceType] =
                recipeFilterParameters.selectedIntoleranceType
            preferences[PreferenceKeys.intoleranceTypeId] =
                recipeFilterParameters.selectedIntoleranceTypeId
        }
    }

    val readRecipeFilterParameter: Flow<RecipeFilterParameters> =
        dataStore.data.catch { exception ->
            if (exception is IOException)
                emit(emptyPreferences())
            else
                throw exception
        }
            .map { preferences ->
                val selectedMealType =
                    preferences[PreferenceKeys.selectedMealType] ?: DEFAULT_MEAL_TYPE
                val selectedMealTypeId = preferences[PreferenceKeys.selectedMealTypeId] ?: 0
                val selectedDietType =
                    preferences[PreferenceKeys.selectedDietType] ?: DEFAULT_DIET
                val selectedDietTypeId = preferences[PreferenceKeys.selectedDietTypeId] ?: 0
                val selectedCuisineType =
                    preferences[PreferenceKeys.cuisineType] ?: DEFAULT_CUISINE
                val selectedCuisineTypeId = preferences[PreferenceKeys.cuisineTypeId] ?: 0
                val selectedIntoleranceType =
                    preferences[PreferenceKeys.intoleranceType] ?: DEFAULT_INTOLERANCE
                val selectedIntoleranceTypeId = preferences[PreferenceKeys.intoleranceTypeId] ?: 0

                Log.e(TAG, "Flow emitted")
                RecipeFilterParameters(
                    selectedMealType,
                    selectedMealTypeId,
                    selectedDietType,
                    selectedDietTypeId,
                    selectedCuisineType,
                    selectedCuisineTypeId,
                    selectedIntoleranceType,
                    selectedIntoleranceTypeId
                )
            }

    data class RecipeFilterParameters(
        var selectedMealType: String,
        var selectedMealTypeId: Int,
        var selectedDietType: String,
        var selectedDietTypeId: Int,
        var selectedCuisineType: String,
        var selectedCuisineTypeId: Int,
        var selectedIntoleranceType: String,
        var selectedIntoleranceTypeId: Int,
    )
}