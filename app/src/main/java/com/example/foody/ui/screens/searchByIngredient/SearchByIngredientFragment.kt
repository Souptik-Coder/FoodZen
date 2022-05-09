package com.example.foody.ui.screens.searchByIngredient

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.foody.R
import com.example.foody.adapters.IngredientSuggestionArrayAdapter
import com.example.foody.adapters.SearchByIngredientAdapter
import com.example.foody.databinding.FragmentSearchByIngredientBinding
import com.example.foody.util.NetworkResults
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SearchByIngredientFragment : Fragment(R.layout.fragment_search_by_ingredient) {

    private val TAG = "SearchByIngredient"
    private val REQUEST_IMAGE_PICK = 1
    private val REQUEST_IMAGE_CAPTURE = 2
    private var canNavigate = false

    private var progressDialog: AlertDialog? = null
    private lateinit var binding: FragmentSearchByIngredientBinding
    private lateinit var suggestionArrayAdapter: IngredientSuggestionArrayAdapter
    private lateinit var ingredientAdapter: SearchByIngredientAdapter
    private val viewModel: SearchByIngredientFragmentViewModel by viewModels()

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.e("onActivityResult", "Req code=$requestCode Res code=$resultCode")
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == Activity.RESULT_OK) {
            val imageBitmap =
                MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, data?.data)
            viewModel.recognizeTextFromImage(imageBitmap)
        } else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            viewModel.recognizeTextFromImage(imageBitmap)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentSearchByIngredientBinding.bind(view)
        setUpRecyclerView()
        setUpAutoCompleteTextView()
        setUpDataObserver()
        binding.captureImage.setOnClickListener {
            handlePickImage()
        }
        binding.searchBtn.setOnClickListener {
            canNavigate = true
            viewModel.getRecipesByIngredient(viewModel.applyQueries())
        }
    }

    private fun handlePickImage() {
        MaterialAlertDialogBuilder(requireContext())
            .setIcon(R.drawable.ic_gallery)
            .setTitle("Get image from")
            .setNegativeButton("Camera") { _, _ ->
                captureImage()
            }
            .setPositiveButton("Gallery") { _, _ ->
                pickImageFromGallery()
            }
            .show()
    }

    private fun setUpAutoCompleteTextView() {
        suggestionArrayAdapter = IngredientSuggestionArrayAdapter(
            requireContext(),
            R.layout.ingredient_suggestion_layout
        )
        binding.autoCompleteEditText.setAdapter(suggestionArrayAdapter)

        binding.autoCompleteEditText.addTextChangedListener {
            if (!it.isNullOrBlank() && !it.startsWith("Ingredient")) {
                viewModel.getIngredientSuggestion(it.toString())
            }
        }

        binding.autoCompleteEditText.setOnItemClickListener { _, _, position, _ ->
            binding.autoCompleteEditText.setText("")
            val clickedIngredient = suggestionArrayAdapter.getItem(position)
            viewModel.addIngredientToList(clickedIngredient)
        }
    }

    private fun setUpRecyclerView() {
        ingredientAdapter = SearchByIngredientAdapter()
        binding.recyclerView.adapter = ingredientAdapter
        ingredientAdapter.setOnRemoveButtonClickCallback { position ->
            viewModel.removeIngredientFromList(position)
        }
    }

    private fun setUpDataObserver() {
        viewModel.detectedIngredientResponse.observe(viewLifecycleOwner) { res ->
            when (res) {
                is NetworkResults.Error -> {
                    showSnackBar(getString(res.messageResId!!))
                    hideProgressDialog()
                }
                is NetworkResults.Loading -> showProgressDialog("Extracting ingredient...")
                is NetworkResults.Success -> {
                    viewModel.setCurrentIngredients(res.data!!)
                    hideProgressDialog()
                }
            }
        }
        viewModel.ingredientSuggestionResponse.observe(viewLifecycleOwner) { res ->
            when (res) {
                is NetworkResults.Error -> showSnackBar(getString(res.messageResId!!))
                is NetworkResults.Loading -> Unit
                is NetworkResults.Success -> suggestionArrayAdapter.setData(res.data!!)
            }
        }

        viewModel.currentIngredient.observe(viewLifecycleOwner) {
            ingredientAdapter.submitList(it)
            binding.ingredientCountTextView.text =
                getString(R.string.ingredient_count_text).format(it.size)
        }

        viewModel.recipeResponse.observe(viewLifecycleOwner) { res ->
            when (res) {
                is NetworkResults.Error -> {
                    showSnackBar(getString(res.messageResId!!))
                    hideProgressDialog()
                }
                is NetworkResults.Loading -> showProgressDialog("Loading Recipes...")
                is NetworkResults.Success -> {
                    hideProgressDialog()
                    if (canNavigate) {
                        val action =
                            SearchByIngredientFragmentDirections.actionSearchByIngredientFragmentToRecipeListFragment(
                                res.data?.toTypedArray() ?: emptyArray()
                            )
                        findNavController().navigate(action)
                        canNavigate = false
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

    private fun pickImageFromGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(
            Intent.createChooser(intent, "Select Picture"),
            REQUEST_IMAGE_PICK
        )
    }

    private fun captureImage() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(requireContext(), "No camera app detected", Toast.LENGTH_LONG).show()
        }
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
    }

}