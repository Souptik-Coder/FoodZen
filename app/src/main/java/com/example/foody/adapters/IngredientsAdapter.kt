package com.example.foody.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foody.databinding.IngredientsRowLayoutBinding
import com.example.foody.models.ExtendedIngredient
import com.example.foody.util.Constants

class IngredientsAdapter : RecyclerView.Adapter<IngredientsAdapter.ViewHolder>() {

    private var ingredients = emptyList<ExtendedIngredient>()

    class ViewHolder(private val binding: IngredientsRowLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(extendedIngredient: ExtendedIngredient) {
            binding.ingredients = extendedIngredient
            binding.executePendingBindings()
            Glide.with(binding.root)
                .load(Constants.BASE_INGREDIENT_IMAGE_URL + extendedIngredient.image)
                .into(binding.ingredientImageView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            IngredientsRowLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(ingredients[position])
    }

    override fun getItemCount(): Int =
        ingredients.size

    fun setData(newData: List<ExtendedIngredient>) {
        ingredients = newData
        notifyDataSetChanged()
    }
}