package com.example.foody.ui.screens.recipes

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foody.R
import com.example.foody.adapters.RecipesAdapter
import com.example.foody.databinding.FragmentRecipesBinding
import com.example.foody.models.Recipe
import com.example.foody.util.NetworkResults
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecipesFragment : Fragment(R.layout.fragment_recipes) {

    private val TAG = "RecipesFragment"

    private val args by navArgs<RecipesFragmentArgs>() //receives id only when deeplink is requested else -1
    private var isDeepLinkRequested = false
    private lateinit var binding: FragmentRecipesBinding
    private val recipesAdapter by lazy { RecipesAdapter() }
    private val recipesViewModel by activityViewModels<RecipesViewModel>()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentRecipesBinding.bind(view)
        isDeepLinkRequested = args.id != -1
        if (isDeepLinkRequested)
            recipesViewModel.getRecipeById(args.id)
        setUpRecyclerView()
        setUpDataObservers()
        binding.recipesFab.setOnClickListener {
            findNavController().navigate(R.id.action_recipesFragment_to_recipesBottomSheet)
        }
    }

    private fun setUpDataObservers() {
        recipesViewModel.topRecipeResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResults.Loading -> {
                    showShimmerEffect()
                }
                is NetworkResults.Success -> {
                    hideShimmerEffect()
                }
                is NetworkResults.Error -> {
                    hideShimmerEffect()
                    if (!response.isErrorHandled) {
                        showSnackBar(getString(response.messageResId!!))
                        recipesViewModel.setErrorHandled()
                    }
                }
            }
        }
        recipesViewModel.recipeByIdResponse.observe(viewLifecycleOwner) { res ->
            when (res) {
                is NetworkResults.Error -> {
                    hideShimmerEffect()
                    showSnackBar(getString(res.messageResId!!))
                }
                is NetworkResults.Loading -> {
                    showShimmerEffect()
                }
                is NetworkResults.Success -> {
                    hideShimmerEffect()
                    handleDeepLink(res.data!!)
                }
            }
        }

        recipesViewModel.savedTopRecipes.observe(viewLifecycleOwner) {
            hideShimmerEffect()
            recipesAdapter.setData(it)
        }
    }

    private fun handleDeepLink(recipe: Recipe) {
        val action = RecipesFragmentDirections.actionRecipesFragmentToDetailsFragment(recipe)
        findNavController().navigate(action)
    }

    private fun showSnackBar(message: String) {
        val snackBar = Snackbar.make(
            binding.root,
            message,
            Snackbar.LENGTH_LONG
        )

        snackBar.setAction("Dismiss") {
            snackBar.dismiss()
        }
        snackBar.show()
    }

    private fun hideErrorTextViewAndImageView() {
        binding.errorImageView.visibility = View.INVISIBLE
        binding.errorTextView.visibility = View.INVISIBLE
    }

    private fun showErrorTextViewAndImageView() {
        binding.errorImageView.visibility = View.VISIBLE
        binding.errorTextView.visibility = View.VISIBLE
    }

    private fun setUpRecyclerView() {
        recipesAdapter.setOnClickListener {
            val action = RecipesFragmentDirections.actionRecipesFragmentToDetailsFragment(it)
            findNavController().navigate(action)
        }
        binding.shimmerRecyclerView.apply {
            adapter = recipesAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun showShimmerEffect() {
        binding.shimmerRecyclerView.showShimmer()
    }

    private fun hideShimmerEffect() {
        binding.shimmerRecyclerView.hideShimmer()
    }
}