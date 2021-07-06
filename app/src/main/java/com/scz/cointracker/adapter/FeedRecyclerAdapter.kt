package com.scz.cointracker.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.scz.cointracker.R
import com.scz.cointracker.databinding.FeedRowBinding
import com.scz.cointracker.model.FeedCoinModel
import com.scz.cointracker.util.castDouble
import com.scz.cointracker.util.format

class FeedRecyclerAdapter constructor(
    private var coinList: MutableList<FeedCoinModel>,
    private var listener: OnRecyclerItemDeleted,
    private var ctx: Context,
    private var clickListener: RowClickListener
) : RecyclerView.Adapter<FeedRecyclerAdapter.ViewHolder>() {

    class ViewHolder(var binding: FeedRowBinding, var ctx: Context) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: FeedCoinModel, listener: RowClickListener) {
            var profit = 0.0
            item.actualPrice?.let {
                profit =
                    (it.castDouble() - item.buyedPrice.castDouble()) * item.buyedUnit.castDouble()
                item.actualPrice = it.format(2)
                item.buyedPrice = item.buyedPrice.format(2)
            }
            if (profit > 0) binding.profit.setTextColor(ContextCompat.getColor(ctx, R.color.green))
            else binding.profit.setTextColor(Color.RED)
            item.difference = profit.format(2)
            binding.viewState = item
            binding.root.setOnClickListener {
                listener.onClick(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.d("OnCreateViewHolder", "${coinList.size} + ${Thread.currentThread()}")
        val binding = DataBindingUtil.inflate<FeedRowBinding>(
            LayoutInflater.from(parent.context),
            R.layout.feed_row,
            parent,
            false
        )
        return ViewHolder(binding, ctx)
    }

    override fun getItemCount() = coinList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.d("OnBindViewHolder", "${coinList.size} + ${Thread.currentThread()}")
        holder.bind(coinList[position], clickListener)
    }

    fun delete(id: Int) {
        listener.OnDelete(coinList[id].recordName)
    }

    interface OnRecyclerItemDeleted {
        fun OnDelete(recordName: String)
    }

    interface RowClickListener {
        fun onClick(coin: FeedCoinModel)
    }
}
