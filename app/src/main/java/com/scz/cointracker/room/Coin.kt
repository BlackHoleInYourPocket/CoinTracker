package com.scz.cointracker.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Coins")
data class Coin(
    var recordName: String,
    var name: String,
    var buyedPrice: Double,
    var buyedUnit: Double,
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
)