package com.example.foody.data.database.typeconverters

import androidx.room.TypeConverter
import com.example.foody.models.ExtendedIngredient
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class TypeConverter {
    @TypeConverter
    fun fromString(stringListString: String): ArrayList<ExtendedIngredient> {
        return Gson().fromJson(stringListString,
            object : TypeToken<ArrayList<ExtendedIngredient>>() {}.type)
    }

    @TypeConverter
    fun toString(stringList: List<ExtendedIngredient>): String {
        return Gson().toJson(stringList)
    }
}