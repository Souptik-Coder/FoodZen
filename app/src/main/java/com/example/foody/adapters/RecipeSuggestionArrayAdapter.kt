package com.example.foody.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.annotation.LayoutRes
import com.bumptech.glide.Glide
import com.example.foody.databinding.IngredientSuggestionLayoutBinding
import com.example.foody.models.RecipeSuggestionItem
import com.example.foody.util.Constants

class RecipeSuggestionArrayAdapter(
    context: Context,
    @LayoutRes private val layoutResId: Int,
) : ArrayAdapter<RecipeSuggestionItem>(context, layoutResId) {

    private var recipes: List<RecipeSuggestionItem> = emptyList()

    override fun getItem(position: Int): RecipeSuggestionItem {
        return recipes[position]
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val recipe = recipes[position]
        return if (convertView != null) {
            val binding = convertView.tag as IngredientSuggestionLayoutBinding
            binding.textView.text = recipe.title
            Glide.with(binding.root)
                .load(Constants.buildRecipeImageUrl(recipe.id, recipe.imageType))
                .into(binding.imageView)
            binding.root
        } else {
            val binding = IngredientSuggestionLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            binding.textView.text = recipe.title.capitalize()
            Glide.with(binding.root)
                .load(Constants.buildRecipeImageUrl(recipe.id, recipe.imageType))
                .into(binding.imageView)
            binding.root.tag = binding
            binding.root
        }
    }

    override fun getCount(): Int {
        return recipes.size
    }

    fun setData(recipes: List<RecipeSuggestionItem>) {
        this.recipes = recipes
        notifyDataSetChanged()
    }
}