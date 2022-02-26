package com.example.foody.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
    data class RecipeListItem(
        val id: Int,
        val image: String,
        val title: String
    ) : Parcelable
