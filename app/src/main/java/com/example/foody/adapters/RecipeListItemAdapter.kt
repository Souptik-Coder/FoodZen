package com.example.foody.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foody.databinding.RecipeListItemBinding
import com.example.foody.models.RecipeListItem

class RecipeListItemAdapter : RecyclerView.Adapter<RecipeListItemAdapter.ViewHolder>() {
    private var recipeListItems = emptyList<RecipeListItem>()
    private var onItemClickListener: ((RecipeListItem) -> Unit)? = null

    inner class ViewHolder(private val binding: RecipeListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                onItemClickListener?.invoke(recipeListItems[adapterPosition]);
            }
        }

        fun bind(recipeListItem: RecipeListItem) {
            binding.textView.text = recipeListItem.title
            Glide.with(binding.root).load(recipeListItem.image).into(binding.imageView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            RecipeListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(recipeListItems[position])
    }

    override fun getItemCount(): Int {
        return recipeListItems.size
    }

    fun setData(recipeListItems: List<RecipeListItem>) {
        this.recipeListItems = recipeListItems
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(onItemClickListener: ((RecipeListItem) -> Unit)) {
        this.onItemClickListener = onItemClickListener;
    }
}