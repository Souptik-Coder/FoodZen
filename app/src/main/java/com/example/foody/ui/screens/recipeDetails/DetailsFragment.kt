package com.example.foody.ui.screens.recipeDetails

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.transition.ChangeBounds
import androidx.transition.ChangeImageTransform
import androidx.transition.TransitionInflater
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.foody.R
import com.example.foody.adapters.PagerAdapter
import com.example.foody.databinding.CustomTabBinding
import com.example.foody.databinding.FragmentDetailsBinding
import com.example.foody.models.Recipe
import com.example.foody.ui.screens.analyzedInstructions.AnalyzedInstructionFragment
import com.example.foody.ui.screens.ingredients.IngredientsFragment
import com.example.foody.ui.screens.moreInfo.InfoFragment
import com.example.foody.ui.screens.overview.OverviewFragment
import com.example.foody.util.NetworkResults
import com.example.foody.util.toImageUri
import com.example.foody.viewmodels.MainViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class DetailsFragment : Fragment(R.layout.fragment_details) {
    private val mainViewModel by activityViewModels<MainViewModel>()
    private val viewModel: DetailsFragmentViewModel by viewModels()
    private lateinit var menuItem: MenuItem
    private var isFavourite = false
    private lateinit var binding: FragmentDetailsBinding
    private lateinit var currentRecipe: Recipe
    private val args by navArgs<DetailsFragmentArgs>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDetailsBinding.bind(view)
        setUpDataObserver()
        currentRecipe = args.recipe
        viewModel.insertOrUpdateRecentlyVisitedRecipe(currentRecipe)

        Glide.with(this).load(args.recipe.image).into(binding.recipeImageview)

        val fragments = ArrayList<Fragment>()
        fragments.add(OverviewFragment())
        fragments.add(IngredientsFragment())
        fragments.add(AnalyzedInstructionFragment())
        fragments.add(InfoFragment())

        val title = ArrayList<String>()
        title.add("Overview")
        title.add("Ingredients")
        title.add("Steps")
        title.add("More Info")

        val resultBundle = Bundle()
        resultBundle.putParcelable("resultBundle", currentRecipe)

        val pagerAdapter = PagerAdapter(
            resultBundle,
            fragments,
            this
        )

        binding.viewPager2.adapter = pagerAdapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager2)
        { tab, position ->
            tab.text = title[position]
            /*tab.customView = CustomTabBinding.inflate(layoutInflater).apply {
            }.root*/
        }.attach()
    }

    private fun setUpDataObserver() {
        viewModel.analyzedInstructionResponse.observe(viewLifecycleOwner) { res ->
            when (res) {
                is NetworkResults.Error -> Unit //Handled by instruction fragment
                is NetworkResults.Loading -> Unit //Handled by instruction fragment
                is NetworkResults.Success -> {
                    val analyzedInstruction = res.data
                    currentRecipe =
                        currentRecipe.copy(analyzedInstruction = analyzedInstruction)
                    viewModel.insertOrUpdateRecentlyVisitedRecipe(currentRecipe)
                    if (isFavourite) {
                        mainViewModel.insertOrUpdateFavouriteRecipe(currentRecipe)
                    }
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.share -> {
                shareRecipeLink()
            }

            R.id.favourite -> {
                if (isFavourite.not()) {
                    saveAndMarkAsFavourite(menuItem)
                    isFavourite = true
                } else {
                    deleteAndUnmarkedAsFavourite(menuItem)
                    isFavourite = false
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun shareRecipeLink() {
        val link = "http://app.FoodZen/recipes/${currentRecipe.id}"
        val message = getString(R.string.link_share_message).format(currentRecipe.title, link)

        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(
                Intent.EXTRA_TEXT,
                message
            )

            putExtra(
                Intent.EXTRA_STREAM,
                binding.recipeImageview.drawable.toBitmap()
                    .toImageUri(requireContext(), currentRecipe.id.toString())
            )
        }
        startActivity(Intent.createChooser(intent, "Share recipe via"))
    }

    private fun deleteAndUnmarkedAsFavourite(menuItem: MenuItem) {
        mainViewModel.deleteFavouriteRecipe(currentRecipe)
        changeMenuItemColor(menuItem, R.color.white)
        Snackbar.make(binding.root, "Recipe removed from favourite", Snackbar.LENGTH_SHORT).show()
    }

    private fun saveAndMarkAsFavourite(menuItem: MenuItem) {
        mainViewModel.insertOrUpdateFavouriteRecipe(currentRecipe)
        changeMenuItemColor(menuItem, R.color.yellow)
        Snackbar.make(binding.root, "Recipe added to favourite", Snackbar.LENGTH_SHORT).show()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.details_activity_menu, menu)
        menuItem = menu.findItem(R.id.favourite)
        mainViewModel.favouriteRecipes.observe(this) { favourites ->
            favourites.forEach { recipe ->
                if (recipe.id == currentRecipe.id) {
                    isFavourite = true
                    changeMenuItemColor(
                        menuItem,
                        R.color.yellow
                    )
                }
            }
        }
    }

    private fun changeMenuItemColor(menuItem: MenuItem, @ColorRes colorResId: Int) {
        menuItem.icon?.setTint(ContextCompat.getColor(requireContext(), colorResId))
    }

    override fun onDestroy() {
        super.onDestroy()
        if (this::menuItem.isInitialized)
            changeMenuItemColor(menuItem, R.color.white)
    }
}