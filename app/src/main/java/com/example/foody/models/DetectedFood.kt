package com.example.foody.models


import com.google.gson.annotations.SerializedName

data class DetectedFood(
    @SerializedName("annotations")
    val annotations: List<Annotation>
)