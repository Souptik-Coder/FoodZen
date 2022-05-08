package com.example.foody.adapters

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class PagerAdapter(
    private val resultBundle: Bundle,
    private var fragments: ArrayList<Fragment>,
    parentFragment: Fragment
) : FragmentStateAdapter(parentFragment) {
    override fun getItemCount(): Int =
        fragments.size

    override fun createFragment(position: Int): Fragment {
        fragments[position].arguments = resultBundle
        return fragments[position]
    }

}