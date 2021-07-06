package com.scz.cointracker.view

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.scz.cointracker.adapter.FeedRecyclerAdapter
import javax.inject.Inject

class CoinFragmentFactory @Inject constructor() : FragmentFactory() {

    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        return when (className) {
            FeedFragment::class.java.name -> FeedFragment()
            DetailFragment::class.java.name -> DetailFragment()
            else -> super.instantiate(classLoader, className)
        }
    }
}