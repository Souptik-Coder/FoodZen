package com.example.foody.ui.screens.search

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.foody.R
import com.example.foody.adapters.RecipeSuggestionArrayAdapter
import com.example.foody.databinding.FragmentSearchBinding
import com.example.foody.ui.screens.recipeDetails.DetailsActivity
import com.example.foody.util.NetworkResults
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentSearchBinding.bind(view)
        setUpAutoCompleteTextView()
        setUpDataObserver()
        binding.searchByIngredientBtn.setOnClickListener {
            val action = SearchFragmentDirections.actionSearchFragmentToSearchByIngredientFragment()
            findNavController().navigate(action)
        }
    }

    private fun setUpDataObserver() {
        viewModel.recipeSuggestionResponse.observe(viewLifecycleOwner) { res ->
            when (res) {
                is NetworkResults.Success -> {
                    suggestionArrayAdapter.setData(res.data!!)
                }
                is NetworkResults.Error -> {
                    Snackbar.make(binding.root, res.messageResId!!, Snackbar.LENGTH_LONG).show()
                }
                is NetworkResults.Loading -> Unit
            }
        }

        viewModel.recipeResponse.observe(viewLifecycleOwner) { res ->
            when (res) {
                is NetworkResults.Success -> {
                    if (canNavigate) {
                        val intent = Intent(requireContext(), DetailsActivity::class.java).apply {
                            putExtra("recipe", res.data?.first())
                        }
                        startActivity(intent)
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