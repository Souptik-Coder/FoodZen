package com.example.foody.ui.fragments.recipeList

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.example.foody.R
import com.example.foody.adapters.RecipeListItemAdapter
import com.example.foody.databinding.FragmentRecipeListBinding
import com.example.foody.util.NetworkResults
import com.example.foody.viewmodels.RecipeListFragmentViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecipeListFragment : Fragment(R.layout.fragment_recipe_list) {
    private val TAG = "RecipeListFragment"
    private val args by navArgs<RecipeListFragmentArgs>()
    private lateinit var binding: FragmentRecipeListBinding
    private val viewModel by viewModels<RecipeListFragmentViewModel>()
    private var progressDialog: AlertDialog? = null
    private var canNavigate = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentRecipeListBinding.bind(view)
        val adapter = RecipeListItemAdapter()
        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.recyclerView.adapter = adapter
        adapter.setData(args.recipeListItems.toList())
        adapter.setOnItemClickListener {
            canNavigate = true
            viewModel.getRecipeById(it.id)
        }
        viewModel.recipeResponse.observe(viewLifecycleOwner) { res ->
            when (res) {
                is NetworkResults.Loading -> {
                    showProgressDialog("Getting recipe details")
                }
                is NetworkResults.Error -> {
                    Snackbar.make(binding.root, res.messageResId!!, Snackbar.LENGTH_LONG).show()
                }
                is NetworkResults.Success -> {
                    if (canNavigate) {
                        val action =
                            RecipeListFragmentDirections.actionGlobalDetailsActivity(res.data?.first()!!)
                        findNavController().navigate(action)
                        hideProgressDialog()
                    }
                }
            }
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