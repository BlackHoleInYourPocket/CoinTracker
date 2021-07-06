package com.scz.cointracker.repository

import androidx.lifecycle.LiveData
import com.scz.cointracker.model.TickerResponse
import com.scz.cointracker.room.Coin
import com.scz.cointracker.room.CoinDao
import com.scz.cointracker.service.BinanceApi
import com.scz.cointracker.util.Resource
import java.lang.Exception
import javax.inject.Inject

class CoinRepository @Inject constructor(
    private val coinDao: CoinDao,
    private val api: BinanceApi
) : ICoinRepository {
    override suspend fun insertCoin(coin: Coin) = coinDao.insertCoin(coin)

    override suspend fun deleteCoin(recordName: String) = coinDao.deleteCoin(recordName)

    override suspend fun getCoin() = coinDao.getCoins()

    override suspend fun getTicker(symbol: String): Resource<TickerResponse> {
        return try {
            val response = api.getTickerPrice(symbol)
            if (response.isSuccessful) {
                response.body()?.let {
                    return@let Resource.success(it)
                } ?: Resource.error("Hata", null)
            } else {
                Resource.error("Hata", null)
            }
        } catch (e: Exception) {
            Resource.error("Hata", null)
        }
    }
}