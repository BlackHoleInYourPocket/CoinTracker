package com.scz.cointracker.room

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface CoinDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCoin(coin: Coin)

    @Query("DELETE FROM coins WHERE recordName = :recordName")
    suspend fun deleteCoin(recordName: String)

    @Query("SELECT * FROM coins")
    suspend fun getCoins(): List<Coin>
}