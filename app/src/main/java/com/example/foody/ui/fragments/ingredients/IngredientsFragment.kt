package com.example.foody.ui.fragments.ingredients

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foody.R
import com.example.foody.adapters.IngredientsAdapter
import com.example.foody.databinding.FragmentIngredientsBinding
import com.example.foody.models.Result

class IngredientsFragment : Fragment(R.layout.fragment_ingredients) {

    private lateinit var binding: FragmentIngredientsBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentIngredientsBinding.bind(view)
        val result: Result? = arguments?.getParcelable("resultBundle")

        val adapter = IngredientsAdapter()
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter
        adapter.setData(result!!.extendedIngredients)

    }
}