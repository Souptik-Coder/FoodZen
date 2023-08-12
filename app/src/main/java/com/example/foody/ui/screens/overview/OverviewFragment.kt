package com.example.foody.ui.screens.overview

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.foody.R
import com.example.foody.databinding.FragmentOverviewBinding
import com.example.foody.models.Recipe


class OverviewFragment : Fragment(R.layout.fragment_overview) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentOverviewBinding.bind(view)
        binding.result = arguments?.getParcelable("resultBundle")
        binding.executePendingBindings()


        binding.idTextView.setOnClickListener { view1 ->
            copyToClipBoard((view1 as TextView).text.filter { it.isDigit() })
        }
    }

    private fun copyToClipBoard(text: CharSequence) {
        val clipboard: ClipboardManager =
            requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Id", text)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(requireContext(), "Id copied", Toast.LENGTH_SHORT).show()
    }
}