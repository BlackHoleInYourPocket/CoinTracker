package com.scz.cointracker.repository

import androidx.lifecycle.LiveData
import com.scz.cointracker.model.TickerResponse
import com.scz.cointracker.room.Coin
import com.scz.cointracker.util.Resource

interface ICoinRepository {
    suspend fun insertCoin(coin: Coin)

    suspend fun deleteCoin(recordName: String)

    suspend fun getCoin(): List<Coin>

    suspend fun getTicker(symbol: String): Resource<TickerResponse>
}