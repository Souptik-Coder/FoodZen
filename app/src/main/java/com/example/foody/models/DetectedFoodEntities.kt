package com.example.foody.models


import com.google.gson.annotations.SerializedName

data class DetectedFoodEntities(
    @SerializedName("annotations")
    val detectedFoodEntities: List<DetectedFoodEntity>
)