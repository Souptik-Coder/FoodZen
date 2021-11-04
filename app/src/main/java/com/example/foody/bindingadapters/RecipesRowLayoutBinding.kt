package com.example.foody.bindingadapters

import android.os.Build
import android.text.Html
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.example.foody.R
import com.example.foody.models.Result
import com.example.foody.ui.fragments.recipes.RecipesFragmentDirections
import com.google.android.material.card.MaterialCardView

class RecipesRowLayoutBinding {
    companion object {

        @BindingAdapter("setHtmlParsedText")
        @JvmStatic
        fun setHtmlParsedText(textView: TextView, text: String) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                textView.text = Html.fromHtml(text, Html.FROM_HTML_MODE_COMPACT).toString()
            } else {
                textView.text = Html.fromHtml(text).toString()
            }
        }

        @BindingAdapter("recipeOnClick")
        @JvmStatic
        fun setOnClickListener(materialCardView: MaterialCardView, result: Result) {
            materialCardView.setOnClickListener {
                val action =
                    RecipesFragmentDirections.actionRecipesFragmentToDetailsActivity(result)
                materialCardView.findNavController().navigate(action)
            }
        }

        @BindingAdapter("loadImageFromUrl")
        @JvmStatic
        fun loadImageFromUrl(imageView: ImageView, url: String) {
            Glide.with(imageView).load(url).into(imageView)
        }


        @BindingAdapter("setGreenColor")
        @JvmStatic
        fun setGreenColor(view: View, isGreen: Boolean) {
            if (isGreen) {
                when (view) {
                    is ImageView -> view.setColorFilter(
                        ContextCompat.getColor(
                            view.context,
                            R.color.green
                        )
                    )
                    is TextView -> {
                        view.setTextColor(
                            ContextCompat.getColor(
                                view.context,
                                R.color.green
                            )
                        )

                        view.compoundDrawablesRelative[0]?.setTint(
                            ContextCompat.getColor(
                                view.context,
                                R.color.green
                            )
                        )
                    }
                }
            }
        }
    }
}