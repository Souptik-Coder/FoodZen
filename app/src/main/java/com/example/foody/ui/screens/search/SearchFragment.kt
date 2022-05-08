package com.example.foody.ui.screens.search

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.foody.R
import com.example.foody.adapters.RecipeSuggestionArrayAdapter
import com.example.foody.adapters.RecipesAdapter
import com.example.foody.databinding.FragmentSearchBinding
import com.example.foody.util.NetworkResults
import com.example.foody.viewmodels.MainViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.fragment_search) {

    private lateinit var binding: FragmentSearchBinding
    private lateinit var suggestionArrayAdapter: RecipeSuggestionArrayAdapter
    private val viewModel: SearchFragmentViewModel by viewModels()
    private var canNavigate = false
    private var progressDialog: AlertDialog? = null
    private val mainViewModel by activityViewModels<MainViewModel>()
    private val recentlyVisitedAdapter by lazy { RecipesAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentSearchBinding.bind(view)
        setUpAutoCompleteTextView()
        setUpDataObserver()
        setUpRecyclerView()
        binding.searchByIngredientBtn.setOnClickListener {
            val action = SearchFragmentDirections.actionSearchFragmentToSearchByIngredientFragment()
            findNavController().navigate(action)
        }

    }

    private fun setUpRecyclerView() {
        recentlyVisitedAdapter.setOnClickListener {
            val action = SearchFragmentDirections.actionSearchFragmentToDetailsFragment(it)
            findNavController().navigate(action)
        }
        binding.recyclerView.adapter = recentlyVisitedAdapter
    }

    private fun setUpDataObserver() {
        mainViewModel.recentlyVisitedRecipes.observe(viewLifecycleOwner) {
            recentlyVisitedAdapter.setData(it)
        }
        viewModel.recipeSuggestionResponse.observe(viewLifecycleOwner) { res ->
            when (res) {
                is NetworkResults.Success -> {
                    suggestionArrayAdapter.setData(res.data!!)
                    binding.swipeRefreshLayout.isRefreshing = false
                }
                is NetworkResults.Error -> {
                    Snackbar.make(binding.root, res.messageResId!!, Snackbar.LENGTH_LONG).show()
                    binding.swipeRefreshLayout.isRefreshing = false
                }
                is NetworkResults.Loading -> {
                    binding.swipeRefreshLayout.isRefreshing = true
                }
            }
        }
        viewModel.recipeResponse.observe(viewLifecycleOwner) { res ->
            when (res) {
                is NetworkResults.Success -> {
                    if (canNavigate) {
                        val action =
                            SearchFragmentDirections.actionSearchFragmentToDetailsFragment(res.data?.first()!!)
                        findNavController().navigate(action)
                        canNavigate = false
                    }
                    hideProgressDialog()
                }
                is NetworkResults.Error -> {
                    Snackbar.make(binding.root, res.messageResId!!, Snackbar.LENGTH_LONG).show()
                    hideProgressDialog()
                }
                is NetworkResults.Loading -> showProgressDialog("Getting Recipe Details...")
            }
        }
    }

    private fun setUpAutoCompleteTextView() {
        suggestionArrayAdapter = RecipeSuggestionArrayAdapter(
            requireContext(),
            R.layout.ingredient_suggestion_layout
        )
        binding.autoCompleteEditText.setAdapter(suggestionArrayAdapter)

        binding.autoCompleteEditText.addTextChangedListener {
            if (!it.isNullOrBlank() && !it.startsWith("RecipeSuggestionItem")) {
                viewModel.getRecipeSuggestions(it.toString())
            }
        }

        binding.autoCompleteEditText.setOnItemClickListener { _, _, position, _ ->
            canNavigate = true
            val recipeId = suggestionArrayAdapter.getItem(position).id
            viewModel.getRecipeById(recipeId)
            binding.autoCompleteEditText.text = null
        }
    }

    private fun showProgressDialog(title: String) {
        progressDialog = MaterialAlertDialogBuilder(requireContext()).setTitle(title)
            .setView(R.layout.progress_dialog).setCancelable(false).show()
    }

    private fun hideProgressDialog() {
        progressDialog?.hide()
    }
}