package com.example.foody.ui.screens.analyzedInstructions

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.foody.R
import com.example.foody.adapters.AnalyzedInstructionAdapter
import com.example.foody.databinding.FragmentAnalyzedInstructionBinding
import com.example.foody.models.Recipe
import com.example.foody.ui.screens.recipeDetails.DetailsActivityViewModel
import com.example.foody.util.NetworkResults
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AnalyzedInstructionFragment : Fragment(R.layout.fragment_analyzed_instruction) {
    private lateinit var binding: FragmentAnalyzedInstructionBinding
    private val viewModel: DetailsActivityViewModel by activityViewModels()
    private val adapter by lazy { AnalyzedInstructionAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAnalyzedInstructionBinding.bind(view)
        setUpDataObservers()
        setUpRecyclerView()
        val recipe: Recipe = arguments?.getParcelable("resultBundle")!!
        if (recipe.analyzedInstruction == null)
            viewModel.getAnalyzedInstructions(recipe.id)
        else
            adapter.setData(recipe.analyzedInstruction.steps)
    }

    private fun setUpRecyclerView() {
        binding.recyclerView.adapter = adapter
    }

    private fun setUpDataObservers() {
        viewModel.analyzedInstructionResponse.observe(viewLifecycleOwner) { res ->
            when (res) {
                is NetworkResults.Error -> Toast.makeText(
                    requireContext(),
                    res.messageResId!!,
                    Toast.LENGTH_LONG
                ).show()
                is NetworkResults.Loading -> Toast.makeText(
                    requireContext(),
                    "Loading",
                    Toast.LENGTH_LONG
                ).show()
                is NetworkResults.Success -> {
                    adapter.setData(res.data?.first()!!.steps)
                }
            }
        }
    }
}