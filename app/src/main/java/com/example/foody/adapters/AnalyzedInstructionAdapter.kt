package com.example.foody.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.amulyakhare.textdrawable.TextDrawable
import com.amulyakhare.textdrawable.util.ColorGenerator
import com.example.foody.R
import com.example.foody.databinding.InstructionRowLayoutBinding
import com.example.foody.models.AnalyzedInstructionItem

class AnalyzedInstructionAdapter : RecyclerView.Adapter<AnalyzedInstructionAdapter.ViewHolder>() {
    private var instructionSteps = emptyList<AnalyzedInstructionItem.Step>()

    inner class ViewHolder(private val binding: InstructionRowLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(instructionStep: AnalyzedInstructionItem.Step) {
            binding.textView.text = instructionStep.step
            val drawable = TextDrawable.builder()
                .buildRound(instructionStep.number.toString(), ColorGenerator.MATERIAL.randomColor)
            binding.imageView.setImageDrawable(drawable)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            InstructionRowLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(instructionSteps[position])
    }

    override fun getItemCount(): Int = instructionSteps.size

    fun setData(newData: List<AnalyzedInstructionItem.Step>) {
        instructionSteps = newData
        notifyDataSetChanged()
    }
}