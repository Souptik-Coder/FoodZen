package com.example.foody.ui.screens.recipecard

import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.foody.R
import com.example.foody.databinding.FragmentRecipeCardBinding
import com.example.foody.util.NetworkResults
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.OutputStream


@AndroidEntryPoint
class RecipeCardFragment : Fragment(R.layout.fragment_recipe_card) {
    private lateinit var binding: FragmentRecipeCardBinding
    private val recipeCardViewModel by activityViewModels<RecipeCardViewModel>()
    private var recipeId: Int = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentRecipeCardBinding.bind(view)
        setUpDataObserver()

        binding.generateButton.setOnClickListener {
            if (binding.recipeIdEditText.text.toString().toIntOrNull() == null)
                binding.textInputLayout.error = "Not a valid recipe id"
            else {
                binding.textInputLayout.error = null
                recipeId = binding.recipeIdEditText.text.toString().toInt()
                recipeCardViewModel.getRecipeCard(recipeId)
            }
        }

        binding.shareButton.setOnClickListener {
            shareImage()
        }
    }

    private fun setUpDataObserver() {
        recipeCardViewModel.recipeCardResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResults.Success -> {
                    binding.progressBar.visibility = View.INVISIBLE
                    Glide.with(this).load(response.data?.url).into(binding.cardImageView)
                }

                is NetworkResults.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }

                is NetworkResults.Error -> {
                    binding.progressBar.visibility = View.INVISIBLE
                    Snackbar.make(
                        binding.root,
                        getString(response.messageResId!!),
                        Snackbar.LENGTH_LONG
                    )
                        .show()
                }
            }
        }
    }

    private fun shareImage() {
        lifecycleScope.launch(Dispatchers.IO) {
            val link = "http://app.FoodZen/recipes/${recipeId}"
            val message = "Take a look at this recipe.More info on $link"
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "image/jpg"
                putExtra(
                    Intent.EXTRA_STREAM,
                    getImageUriFromBitmap(binding.cardImageView.drawable.toBitmap())
                )
                putExtra(Intent.EXTRA_TEXT, message)
            }
            startActivity(Intent.createChooser(intent, "Share recipe card via..."))
        }
    }

    private fun getImageUriFromBitmap(bitmap: Bitmap): Uri? {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val filename = "${recipeId}.jpg"
            var fos: OutputStream?
            var imageUri: Uri?
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                put(MediaStore.Video.Media.IS_PENDING, 1)
            }

            //use application context to get contentResolver
            val contentResolver = activity?.contentResolver

            contentResolver.also { resolver ->
                imageUri =
                    resolver?.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
                fos = imageUri?.let { resolver?.openOutputStream(it) }
            }

            fos?.use { bitmap.compress(Bitmap.CompressFormat.JPEG, 70, it) }

            contentValues.clear()
            contentValues.put(MediaStore.Video.Media.IS_PENDING, 0)
            imageUri?.let { contentResolver?.update(it, contentValues, null, null) }

            return imageUri
        } else return null
    }
}