package com.scz.cointracker.view

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.scz.cointracker.R
import com.scz.cointracker.databinding.FragmentDetailBinding
import com.scz.cointracker.model.FeedCoinModel
import com.scz.cointracker.util.castDouble
import com.scz.cointracker.util.format
import javax.inject.Inject

class DetailFragment @Inject constructor() : Fragment(R.layout.fragment_detail) {

    private lateinit var binding: FragmentDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            setScreen(DetailFragmentArgs.fromBundle(it).Coin)
        }
        setOnBackPressed()
        setBackButton()
    }

    private fun setBackButton() {
        binding.back.setOnClickListener {
            back()
        }
    }

    private fun setOnBackPressed() {
        requireActivity().onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                back()
            }
        })
    }

    private fun back() {
        val action = DetailFragmentDirections.actionDetailFragmentToFeedFragment(false)
        findNavController().navigate(action)
    }

    private fun setScreen(coin: FeedCoinModel) {
        val totalBuy = coin.buyedUnit.castDouble() * coin.buyedPrice.castDouble()
        val totalSell = coin.buyedUnit.castDouble() * (coin.actualPrice?.castDouble() ?: 0.0)
        binding.totalBuy.text = totalBuy.format(2)
        binding.totalSell.text = totalSell.format(2)
        if (totalSell > totalBuy) binding.totalSell.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.green
            )
        )
        else binding.totalSell.setTextColor(Color.RED)

        var profit = 0.0
        coin.actualPrice?.let {
            profit =
                (it.castDouble() - coin.buyedPrice.castDouble()) * coin.buyedUnit.castDouble()
            coin.actualPrice = it.format(2)
            coin.buyedPrice = coin.buyedPrice.format(2)
        }
        if (profit > 0) binding.difference.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.green
            )
        )
        else binding.difference.setTextColor(Color.RED)
        binding.viewState = coin
    }
}