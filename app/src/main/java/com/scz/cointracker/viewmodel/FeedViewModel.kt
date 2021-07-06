package com.scz.cointracker.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.scz.cointracker.model.FeedCoinModel
import com.scz.cointracker.model.TickerResponse
import com.scz.cointracker.repository.ICoinRepository
import com.scz.cointracker.room.Coin
import com.scz.cointracker.util.Resource
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

class FeedViewModel @ViewModelInject constructor(
    private val repo: ICoinRepository
) : ViewModel() {

    private val _coinsPrices = MutableLiveData<ArrayList<FeedCoinModel>>()
    val coinPrices: LiveData<ArrayList<FeedCoinModel>> = _coinsPrices

    private val _coinList = MutableLiveData<List<Coin>>()
    val coinList: LiveData<List<Coin>> = _coinList

    private val _coinDeleted = MutableLiveData<Boolean>()
    val coinDeleted: LiveData<Boolean> = _coinDeleted

    private var actualPrices = arrayListOf<FeedCoinModel>()
    private var neededPrices = arrayListOf<Coin>()

    var isObservable = false;

    fun getTicker(coins: List<Coin>) {
        neededPrices.clear()
        actualPrices.clear()
        neededPrices.addAll(coins)
        for (coin in coins) {
            viewModelScope.launch {
                val response = repo.getTicker(coin.name.toUpperCase(Locale.ENGLISH).plus("TRY"))
                actualPrices.add(
                    FeedCoinModel(
                        coin.recordName,
                        coin.name,
                        coin.buyedPrice.toString(),
                        response.data?.price?.toDouble()?.toString(),
                        coin.buyedUnit.toString(),
                        "0.0"
                    )
                )
                setPrices(actualPrices)
            }
        }
    }

    fun getCoins() {
        viewModelScope.launch {
            _coinList.postValue(repo.getCoin())
            isObservable = true
        }
    }

    fun deleteCoin(recordName: String) {
        viewModelScope.launch {
            repo.deleteCoin(recordName)
            _coinDeleted.postValue(true)
        }
    }

    private fun setPrices(prices: ArrayList<FeedCoinModel>) {
        if (prices.size == neededPrices.size) {
            prices.sortBy { x -> x.recordName }
            _coinsPrices.postValue(prices)
        }
    }
}