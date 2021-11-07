package com.example.foody.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.annotation.ColorRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.navArgs
import com.example.foody.R
import com.example.foody.adapters.PagerAdapter
import com.example.foody.databinding.ActivityDetailsBinding
import com.example.foody.ui.fragments.ingredients.IngredientsFragment
import com.example.foody.ui.fragments.instructions.InstructionsFragment
import com.example.foody.ui.fragments.overview.OverviewFragment
import com.example.foody.viewmodels.MainViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class DetailsActivity : AppCompatActivity() {

    private val args by navArgs<DetailsActivityArgs>()
    private val mainViewModel by viewModels<MainViewModel>()
    private lateinit var menuItem: MenuItem
    private var isFavourite = false
    private lateinit var binding: ActivityDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        binding.toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        val fragments = ArrayList<Fragment>()
        fragments.add(OverviewFragment())
        fragments.add(IngredientsFragment())
        fragments.add(InstructionsFragment())

        val title = ArrayList<String>()
        title.add("Overview")
        title.add("Ingredients")
        title.add("More Info")

        val resultBundle = Bundle()
        resultBundle.putParcelable("resultBundle", args.recipe)

        val pagerAdapter = PagerAdapter(
            resultBundle,
            fragments,
            this
        )

        binding.viewPager2.adapter = pagerAdapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager2) { tab, position ->
            tab.text = title[position]
        }.attach()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
            R.id.share -> {
                shareRecipeLink()
            }
            R.id.favourite -> {
                if (isFavourite.not())
                    saveAndMarkAsFavourite(menuItem)
                else
                    deleteAndUnmarkedAsFavourite(menuItem)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun shareRecipeLink() {
        val link = "https://smart-liv.com/FoodZen/recipes/${args.recipe.id}"
        val message = getString(R.string.link_share_message).format(args.recipe.title, link)

        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(
                Intent.EXTRA_TEXT,
                message
            )
        }
        startActivity(Intent.createChooser(intent, "Share recipe card via"))
    }

    private fun deleteAndUnmarkedAsFavourite(menuItem: MenuItem) {
        mainViewModel.deleteFavouriteRecipe(args.recipe)
        changeMenuItemColor(menuItem, R.color.white)
        Snackbar.make(binding.root, "Recipe removed from favourite", Snackbar.LENGTH_SHORT).show()
    }

    private fun saveAndMarkAsFavourite(menuItem: MenuItem) {
        mainViewModel.insertFavouriteRecipe(args.recipe)
        changeMenuItemColor(menuItem, R.color.yellow)
        Snackbar.make(binding.root, "Recipe added to favourite", Snackbar.LENGTH_SHORT).show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.details_activity_menu, menu)
        menuItem = menu!!.findItem(R.id.favourite)
        mainViewModel.favouriteRecipes.observe(this) { favourites ->
            favourites.forEach {
                if (it.id == args.recipe.id) {
                    isFavourite = true
                    changeMenuItemColor(
                        menuItem,
                        R.color.yellow
                    )
                }
            }
        }
        return true
    }

    private fun changeMenuItemColor(menuItem: MenuItem, @ColorRes colorResId: Int) {
        menuItem.icon.setTint(ContextCompat.getColor(this, colorResId))
    }

    override fun onDestroy() {
        super.onDestroy()
        if (this::menuItem.isInitialized)
            changeMenuItemColor(menuItem, R.color.white)
    }
}