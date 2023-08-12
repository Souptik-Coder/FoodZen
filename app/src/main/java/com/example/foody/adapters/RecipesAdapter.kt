package com.example.foody.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.foody.databinding.ReceipesRowLayoutBinding
import com.example.foody.models.Recipe

class RecipesAdapter : ListAdapter<Recipe, RecipesAdapter.ViewHolder>(object :
    DiffUtil.ItemCallback<Recipe>() {
    override fun areItemsTheSame(oldItem: Recipe, newItem: Recipe): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Recipe, newItem: Recipe): Boolean {
        return oldItem == newItem
    }

}) {

    private var onItemClickListener: ((Recipe) -> Unit)? = null

    inner class ViewHolder(private val binding: ReceipesRowLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                onItemClickListener?.invoke(getItem(adapterPosition))
            }
        }

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
        holder.bind(getItem(position))
    }

    fun setData(recipes: List<Recipe>) {
        submitList(recipes)
    }

    fun setOnClickListener(onItemClickListener: ((Recipe) -> Unit)) {
        this.onItemClickListener = onItemClickListener
    }
}