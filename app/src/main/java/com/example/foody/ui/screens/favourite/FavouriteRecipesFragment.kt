package com.example.foody.ui.screens.favourite

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foody.R
import com.example.foody.adapters.RecipesAdapter
import com.example.foody.databinding.FragmentFavouriteRecipesBinding
import com.example.foody.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavouriteRecipesFragment : Fragment(R.layout.fragment_favourite_recipes) {
    private val mainViewModel by activityViewModels<MainViewModel>()
    private lateinit var binding: FragmentFavouriteRecipesBinding
    private val recipesAdapter by lazy { RecipesAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentFavouriteRecipesBinding.bind(view)
        setUpRecyclerView()

        mainViewModel.favouriteRecipes.observe(viewLifecycleOwner) {
            if (it.isEmpty()) {
                showErrorTextViewAndImageView()
                recipesAdapter.setData(emptyList())
            } else {
                recipesAdapter.setData(it)
                hideErrorTextViewAndImageView()
            }
        }
    }

    private fun setUpRecyclerView() {
        recipesAdapter.setOnClickListener {
            val action =
                FavouriteRecipesFragmentDirections.actionFavouriteRecipesFragmentToDetailsFragment(
                    it
                )
            findNavController().navigate(action)
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = recipesAdapter
    }

    private fun hideErrorTextViewAndImageView() {
        binding.errorImageView.visibility = View.INVISIBLE
        binding.errorTextView.visibility = View.INVISIBLE
    }

    private fun showErrorTextViewAndImageView() {
        binding.errorImageView.visibility = View.VISIBLE
        binding.errorTextView.visibility = View.VISIBLE
    }
}