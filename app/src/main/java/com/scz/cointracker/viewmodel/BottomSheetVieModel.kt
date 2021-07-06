package com.scz.cointracker.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.scz.cointracker.model.TickerResponse
import com.scz.cointracker.repository.ICoinRepository
import com.scz.cointracker.room.Coin
import com.scz.cointracker.util.Resource
import kotlinx.coroutines.launch

class BottomSheetVieModel @ViewModelInject constructor(
    private val repo: ICoinRepository
) : ViewModel() {
    
    fun saveCoin(coin: Coin) {
        viewModelScope.launch {
            repo.insertCoin(coin)
        }
    }
}