package com.example.foody.ui.fragments.recipes

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foody.R
import com.example.foody.adapters.RecipesAdapter
import com.example.foody.databinding.FragmentRecipesBinding
import com.example.foody.util.NetworkResults
import com.example.foody.viewmodels.MainViewModel
import com.example.foody.viewmodels.RecipesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class RecipesFragment : Fragment(R.layout.fragment_recipes) {

    private val args by navArgs<RecipesFragmentArgs>()
    private lateinit var binding: FragmentRecipesBinding
    private val recipesAdapter by lazy { RecipesAdapter() }
    private val mainViewModel by viewModels<MainViewModel>()
    private val recipesViewModel by viewModels<RecipesViewModel>()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentRecipesBinding.bind(view)
        if (savedInstanceState == null) {
            setUpRecyclerView()
            setUpDataObservers()
            binding.recipesFab.setOnClickListener {
                findNavController().navigate(R.id.action_recipesFragment_to_recipesBottomSheet)
            }
        }
    }

    private fun setUpDataObservers() {
        mainViewModel.recipeResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResults.Loading -> {
                    hideErrorTextViewAndImageView()
                    showShimmerEffect()
                }
                is NetworkResults.Success -> {
                    if (args.id == -1) {
                        hideShimmerEffect()
                        hideErrorTextViewAndImageView()
                        response.data?.let { recipesAdapter.setData(it) }
                    } else {
                        val action =
                            RecipesFragmentDirections.actionRecipesFragmentToDetailsActivity(
                                response.data!!.results.first()
                            )
                        findNavController().navigate(action)
                        requireActivity().finish()
                    }
                }
                is NetworkResults.Error -> {
                    hideShimmerEffect()
                    binding.errorImageView.visibility = View.VISIBLE
                    binding.errorTextView.visibility = View.VISIBLE
                    binding.errorTextView.text = response.message
                    response.data?.let { recipesAdapter.setData(it) }
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            recipesViewModel.readRecipeFilterParameter.collect {
                if (args.id == -1)
                    requestApiData()
                else
                    mainViewModel.getRecipeById(args.id)
            }
        }
    }

    private fun hideErrorTextViewAndImageView() {
        binding.errorImageView.visibility = View.INVISIBLE
        binding.errorTextView.visibility = View.INVISIBLE
    }

    private fun setUpRecyclerView() {
        binding.shimmerRecyclerView.apply {
            adapter = recipesAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
        showShimmerEffect()
    }

    private fun requestApiData() {
        mainViewModel.getRecipes(recipesViewModel.applyQueries())
    }


    private fun showShimmerEffect() {
        binding.shimmerRecyclerView.showShimmer()
    }

    private fun hideShimmerEffect() {
        binding.shimmerRecyclerView.hideShimmer()
    }
}