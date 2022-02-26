package com.example.foody.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.foody.databinding.ReceipesRowLayoutBinding
import com.example.foody.models.RecipeList
import com.example.foody.models.Recipe

class RecipesAdapter : RecyclerView.Adapter<RecipesAdapter.ViewHolder>() {
    private var recipes = emptyList<Recipe>()

    class ViewHolder(private val binding: ReceipesRowLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(recipe: Recipe) {
            binding.result = recipe
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

    fun setData(recipes:List<Recipe>) {
        this.recipes = recipes
//        val recipesDiffUtil = RecipesDiffUtil(newData.results, recipes)
//        val diffUtilResult = DiffUtil.calculateDiff(recipesDiffUtil)
//        diffUtilResult.dispatchUpdatesTo(this)
        notifyDataSetChanged()
    }
}