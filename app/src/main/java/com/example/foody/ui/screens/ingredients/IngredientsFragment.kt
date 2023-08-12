package com.example.foody.ui.screens.ingredients

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foody.R
import com.example.foody.adapters.IngredientsAdapter
import com.example.foody.databinding.FragmentIngredientsBinding
import com.example.foody.models.Recipe

class IngredientsFragment : Fragment(R.layout.fragment_ingredients) {

    private lateinit var binding: FragmentIngredientsBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentIngredientsBinding.bind(view)
        val recipe: Recipe? = arguments?.getParcelable("resultBundle", Recipe::class.java)

        val adapter = IngredientsAdapter()
        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.recyclerView.adapter = adapter
        adapter.setData(recipe!!.extendedIngredients)
    }
}