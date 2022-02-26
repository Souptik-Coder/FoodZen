package com.example.foody.ui.fragments.recipeList

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.foody.R
import com.example.foody.adapters.RecipeListItemAdapter
import com.example.foody.databinding.FragmentRecipeListBinding

class RecipeListFragment : Fragment(R.layout.fragment_recipe_list) {
    private val TAG = "RecipeListFragment"
    private val args by navArgs<RecipeListFragmentArgs>()
    private lateinit var binding: FragmentRecipeListBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentRecipeListBinding.bind(view)
        val adapter = RecipeListItemAdapter()
        binding.recyclerView.adapter = adapter
        adapter.setData(args.recipeListItems.toList())
        Log.e(TAG, args.recipeListItems.size.toString())
    }


}