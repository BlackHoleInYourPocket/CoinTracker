package com.scz.cointracker.model

import java.io.Serializable

class FeedCoinModel(
    val recordName: String,
    val coinSymbol: String,
    var buyedPrice: String,
    var actualPrice: String? = "0.0",
    val buyedUnit: String,
    var difference: String
) : Serializable