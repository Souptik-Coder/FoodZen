package com.example.foody.ui.fragments.search

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.foody.R
import com.example.foody.databinding.FragmentSearchBinding

class SearchFragment : Fragment(R.layout.fragment_search) {

    private lateinit var binding: FragmentSearchBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentSearchBinding.bind(view)
        binding.searchByIngredientBtn.setOnClickListener {
            val action = SearchFragmentDirections.actionSearchFragmentToSearchByIngredientFragment()
            findNavController().navigate(action)
        }
    }
}