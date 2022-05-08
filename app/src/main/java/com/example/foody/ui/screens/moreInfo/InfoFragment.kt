package com.example.foody.ui.screens.moreInfo

import android.os.Bundle
import android.view.View
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.example.foody.R
import com.example.foody.databinding.FragmentInfoBinding
import com.example.foody.models.Recipe

class InfoFragment : Fragment(R.layout.fragment_info) {

    private lateinit var binding: FragmentInfoBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentInfoBinding.bind(view)
        val recipe: Recipe? = arguments?.getParcelable("resultBundle")
        binding.webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                binding.progressBar.visibility = View.GONE
            }

            override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError?
            ) {
                super.onReceivedError(view, request, error)
                showError(R.string.unknown_error)
            }
        }
        binding.webView.loadUrl(recipe!!.sourceUrl)
        binding.retryBtn.setOnClickListener {
            binding.webView.loadUrl(recipe.sourceUrl)
            binding.progressBar.visibility = View.VISIBLE
            hideError()
        }
    }

    private fun showError(@StringRes messageResId: Int) {
        binding.errorLayout.visibility = View.VISIBLE
        binding.errorTextView.text = getString(messageResId)
        binding.webView.visibility = View.INVISIBLE
    }

    private fun hideError() {
        binding.errorLayout.visibility = View.INVISIBLE
        binding.webView.visibility = View.VISIBLE
    }
}