package com.example.foody.ui.fragments.overview

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.View
import androidx.fragment.app.Fragment
import com.example.foody.R
import com.example.foody.databinding.FragmentOverviewBinding

class OverviewFragment : Fragment(R.layout.fragment_overview) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentOverviewBinding.bind(view)
        binding.result = arguments?.getParcelable("resultBundle")
        binding.executePendingBindings()

        binding.summaryTextView.movementMethod = ScrollingMovementMethod()
    }
}