package com.example.foody.ui.fragments.recipes

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foody.R
import com.example.foody.adapters.RecipesAdapter
import com.example.foody.data.repositories.DataStoreRepository
import com.example.foody.databinding.FragmentRecipesBinding
import com.example.foody.models.RecipeList
import com.example.foody.util.NetworkResults
import com.example.foody.viewmodels.MainViewModel
import com.example.foody.viewmodels.RecipesViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RecipesFragment : Fragment(R.layout.fragment_recipes) {

    private val TAG = "RecipesFragment"

    private val args by navArgs<RecipesFragmentArgs>()
    private lateinit var binding: FragmentRecipesBinding
    private val recipesAdapter by lazy { RecipesAdapter() }
    private val mainViewModel by viewModels<MainViewModel>()
    private val recipesViewModel by viewModels<RecipesViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            if (args.id == -1)
                mainViewModel.viewModelScope.launch {
                    recipesViewModel.readRecipeFilterParameter.collectLatest {
                        requestApiData(it)
                    }
                }
            else
                mainViewModel.getRecipeById(args.id)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentRecipesBinding.bind(view)
        setUpRecyclerView()
        setUpDataObservers()
        binding.recipesFab.setOnClickListener {
            findNavController().navigate(R.id.action_recipesFragment_to_recipesBottomSheet)
        }
    }

    private fun requestApiData(recipeFilterParameters: DataStoreRepository.RecipeFilterParameters) {
        mainViewModel.getRecipes(recipesViewModel.applyQueries(recipeFilterParameters))
    }

    private fun setUpDataObservers() {
        mainViewModel.recipeResponse.observe(requireActivity()) { response ->
            Log.d(TAG, "LiveData RecipeResponse received")
            when (response) {
                is NetworkResults.Loading -> {
                    hideErrorTextViewAndImageView()
                    showShimmerEffect()
                }
                is NetworkResults.Success -> {
                    if (args.id == -1) {
                        hideShimmerEffect()
                        hideErrorTextViewAndImageView()
                        recipesAdapter.setData(response.data!!)
                    }
                    // Deep Link requested
                    else {
                        val action =
                            RecipesFragmentDirections.actionGlobalDetailsActivity(
                                response.data!!.recipeList.first()
                            )
                        findNavController().navigate(action)
                        requireActivity().finish()
                    }
                }

                is NetworkResults.InternetError -> {
                    showSnackbar(response.message!!)
                    hideErrorTextViewAndImageView()
                    hideShimmerEffect()
                    showCachedData()
                }

                is NetworkResults.Error -> {
                    hideShimmerEffect()
                    showErrorTextViewAndImageView()
                    binding.errorTextView.text = response.message
                    response.data?.let { recipesAdapter.setData(it) }
                }
            }
        }
    }

    private fun showSnackbar(message: String) {
        val snackbar = Snackbar.make(
            binding.root,
            message,
            Snackbar.LENGTH_INDEFINITE
        )

        snackbar.setAction("Dismiss") {
            snackbar.dismiss()
        }
        snackbar.show()
    }

    private fun showCachedData() {
        mainViewModel.getAllRecentRecipes()
        mainViewModel.recentRecipes.observe(viewLifecycleOwner) { cachedData ->
            if (mainViewModel.recipeResponse.value is NetworkResults.InternetError) {
                if (cachedData.isNotEmpty()) {
                    recipesAdapter.setData(RecipeList(cachedData))
                    hideErrorTextViewAndImageView()
                } else
                    showErrorTextViewAndImageView()
            }
        }
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