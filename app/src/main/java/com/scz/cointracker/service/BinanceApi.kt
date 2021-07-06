package com.scz.cointracker.service

import com.scz.cointracker.model.TickerResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface BinanceApi {
    @GET("ticker/price")
    suspend fun getTickerPrice(@Query("symbol") symbol: String?): Response<TickerResponse>
}