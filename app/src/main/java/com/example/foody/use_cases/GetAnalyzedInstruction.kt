package com.example.foody.use_cases

import com.example.foody.R
import com.example.foody.data.repositories.Repository
import com.example.foody.models.AnalyzedInstructionItem
import com.example.foody.util.NetworkResults
import java.io.IOException

class GetAnalyzedInstruction(
    private val repository: Repository
) {
    suspend operator fun invoke(id: Int): NetworkResults<AnalyzedInstructionItem> {
       return  try {
            val response = repository.remote.getAnalyzedInstruction(id)
            if (response.isSuccessful) {
                NetworkResults.Success(response.body()!!)
                val analyzedInstructionItems=response.body()
                if (analyzedInstructionItems.isNullOrEmpty())
                    NetworkResults.Error(R.string.empty_analyzed_instruction_error_message)
                else
                    NetworkResults.Success(analyzedInstructionItems.first())
            }
            else {
                NetworkResults.Error(R.string.unknown_error)
            }
        } catch (e: IOException) {
            NetworkResults.Error(R.string.internet_error)
        } catch (e: Exception) {
            NetworkResults.Error(R.string.unknown_error)
        }
    }
}