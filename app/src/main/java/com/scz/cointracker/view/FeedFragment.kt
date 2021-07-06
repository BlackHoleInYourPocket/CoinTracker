package com.scz.cointracker.view

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.scz.cointracker.R
import com.scz.cointracker.adapter.FeedRecyclerAdapter
import com.scz.cointracker.adapter.SwipeToDelete
import com.scz.cointracker.databinding.FragmentFeedBinding
import com.scz.cointracker.model.FeedCoinModel
import com.scz.cointracker.room.Coin
import com.scz.cointracker.util.castDouble
import com.scz.cointracker.util.format
import com.scz.cointracker.viewmodel.FeedViewModel
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class FeedFragment @Inject constructor() : Fragment(R.layout.fragment_feed) {

    private lateinit var binding: FragmentFeedBinding
    lateinit var viewModel: FeedViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindVieModel()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_feed, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            viewModel.isObservable = FeedFragmentArgs.fromBundle(it).isObservable
        }
        observeLiveData()
        setRefreshLayout()
        setBottomSheet()
    }

    private fun bindVieModel() {
        viewModel = ViewModelProvider(requireActivity()).get(FeedViewModel::class.java)
        viewModel.getCoins()
    }

    private fun observeLiveData() {
        viewModel.coinPrices.observe(viewLifecycleOwner, Observer {
            if (viewModel.isObservable) {
                setAdapter(it)
            }
        })

        viewModel.coinList.observe(viewLifecycleOwner, Observer { coins ->
            if (viewModel.isObservable) {
                viewModel.getTicker(coins)
            }
        })

        viewModel.coinDeleted.observe(viewLifecycleOwner, Observer { isDeleted ->
            if (isDeleted)
                viewModel.getCoins()
        })
    }

    private fun setAdapter(coinList: MutableList<FeedCoinModel>) {
        Log.d("Set Adapter", "${coinList.size}")
        binding.coinrv.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        val adapter =
            FeedRecyclerAdapter(coinList, object : FeedRecyclerAdapter.OnRecyclerItemDeleted {
                override fun OnDelete(recordName: String) {
                    viewModel.deleteCoin(recordName)
                }
            }, requireContext(), object : FeedRecyclerAdapter.RowClickListener {
                override fun onClick(coin: FeedCoinModel) {
                    val action = FeedFragmentDirections.actionFeedFragmentToDetailFragment(coin)
                    findNavController().navigate(action)
                }
            })
        val helper = ItemTouchHelper(SwipeToDelete(adapter))
        helper.attachToRecyclerView(binding.coinrv)
        binding.coinrv.adapter = adapter
    }

    private fun setRefreshLayout() {
        binding.srl.setOnRefreshListener {
            viewModel.getCoins()
            binding.srl.isRefreshing = false
        }
    }

    private fun setBottomSheet() {
        binding.addCoin.setOnClickListener {
            val bottomSheet = BottomSheet()
            bottomSheet.setDismissListener(object : BottomSheet.BottomSheetDismissListener {
                override fun onDismiss() {
                    viewModel.getCoins()
                }
            })
            bottomSheet.show(parentFragmentManager, "Add Coin")
        }
    }
}