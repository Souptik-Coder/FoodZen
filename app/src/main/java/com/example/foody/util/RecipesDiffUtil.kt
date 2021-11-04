package com.example.foody.util

import androidx.recyclerview.widget.DiffUtil
import com.example.foody.models.Result

class RecipesDiffUtil<T>(
    private val newList: List<T>,
    private val oldList: List<T>) :
    DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        newList === oldList

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        newList == oldList
}