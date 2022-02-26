package com.example.foody.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foody.databinding.FindByIngredientLayoutBinding
import com.example.foody.models.Ingredient

class SearchByIngredientAdapter :
    ListAdapter<Ingredient, SearchByIngredientAdapter.ViewHolder>(DIFF_CALLBACK) {

    private var onRemoveButtonClickCallback: ((Int) -> Unit)? = null


    inner class ViewHolder(private val binding: FindByIngredientLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.removeBtn.setOnClickListener {
                onRemoveButtonClickCallback?.invoke(adapterPosition)
            }
        }

        fun bind(ingredient: Ingredient) {
            binding.textView.text = ingredient.name.capitalize()
            Glide.with(binding.root)
                .load(ingredient.imageUrl)
                .into(binding.imageView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            FindByIngredientLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun setOnRemoveButtonClickCallback(onRemoveButtonClickCallback: (Int) -> Unit) {
        this.onRemoveButtonClickCallback = onRemoveButtonClickCallback
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Ingredient>() {
            override fun areItemsTheSame(oldItem: Ingredient, newItem: Ingredient): Boolean =
                oldItem === newItem

            override fun areContentsTheSame(oldItem: Ingredient, newItem: Ingredient): Boolean =
                oldItem.name == newItem.name

        }
    }
}