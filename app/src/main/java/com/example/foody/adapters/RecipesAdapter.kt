package com.example.foody.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.foody.databinding.ReceipesRowLayoutBinding
import com.example.foody.models.FoodRecipes
import com.example.foody.models.Result
import com.example.foody.util.RecipesDiffUtil

class RecipesAdapter : RecyclerView.Adapter<RecipesAdapter.ViewHolder>() {
    private var recipes = emptyList<Result>()

    class ViewHolder(private val binding: ReceipesRowLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(result: Result) {
            binding.result = result
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            ViewHolder {
        return ViewHolder(
            ReceipesRowLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(recipes[position])
    }

    override fun getItemCount(): Int = recipes.size

    fun setData(newData: FoodRecipes) {
        recipes = newData.results
        val recipesDiffUtil = RecipesDiffUtil(newData.results, recipes)
        val diffUtilResult = DiffUtil.calculateDiff(recipesDiffUtil)
        diffUtilResult.dispatchUpdatesTo(this)
    }
}