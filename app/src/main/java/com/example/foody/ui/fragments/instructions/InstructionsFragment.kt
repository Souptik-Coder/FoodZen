package com.example.foody.ui.fragments.instructions

import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import com.example.foody.R
import com.example.foody.databinding.FragmentInstructionsBinding
import com.example.foody.models.Result

class InstructionsFragment : Fragment(R.layout.fragment_instructions) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentInstructionsBinding.bind(view)
        val result: Result? = arguments?.getParcelable("resultBundle")
        binding.webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                binding.progressBar.visibility = View.GONE
            }
        }
        binding.webView.loadUrl(result!!.sourceUrl)
    }
}