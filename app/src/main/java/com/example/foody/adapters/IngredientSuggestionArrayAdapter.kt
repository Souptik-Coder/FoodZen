package com.example.foody.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.annotation.LayoutRes
import com.bumptech.glide.Glide
import com.example.foody.databinding.IngredientSuggestionLayoutBinding
import com.example.foody.models.Ingredient

class IngredientSuggestionArrayAdapter(
    context: Context,
    @LayoutRes private val layoutResId: Int,
) : ArrayAdapter<Ingredient>(context, layoutResId) {

    private var ingredients: List<Ingredient> = emptyList()

    override fun getItem(position: Int): Ingredient {
        return ingredients[position]
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val ingredient = ingredients[position]
        return if (convertView != null) {
            val binding = convertView.tag as IngredientSuggestionLayoutBinding
            binding.textView.text = ingredient.name
            Glide.with(binding.root).load(ingredient.imageUrl).into(binding.imageView)
            binding.root
        } else {
            val binding = IngredientSuggestionLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            binding.textView.text = ingredient.name.capitalize()
            Glide.with(binding.root).load(ingredient.imageUrl).into(binding.imageView)
            binding.root.tag = binding
            binding.root
        }
    }

    override fun getCount(): Int {
        return ingredients.size
    }

    fun setData(ingredients: List<Ingredient>) {
        this.ingredients = ingredients
        notifyDataSetChanged()
    }
}