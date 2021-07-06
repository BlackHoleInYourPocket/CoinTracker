package com.scz.cointracker.di

import android.content.Context
import androidx.room.Room
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.scz.cointracker.R
import com.scz.cointracker.repository.CoinRepository
import com.scz.cointracker.repository.ICoinRepository
import com.scz.cointracker.room.CoinDao
import com.scz.cointracker.room.CoinDatabase
import com.scz.cointracker.service.BinanceApi
import com.scz.cointracker.service.ServiceUrl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun injectRoomDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(
            context, CoinDatabase::class.java, "CoinDb"
        ).build()

    @Singleton
    @Provides
    fun injectDat(database: CoinDatabase) = database.coinDao()

    @Singleton
    @Provides
    fun injectBinanceApi(): BinanceApi {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(ServiceUrl.BASE_URL)
            .build().create(BinanceApi::class.java)
    }

    @Singleton
    @Provides
    fun injectCoinRepository(dao: CoinDao, api: BinanceApi) =
        CoinRepository(dao, api) as ICoinRepository

    @Singleton
    @Provides
    fun injectGlide(@ApplicationContext context: Context) =
        Glide.with(context).setDefaultRequestOptions(
            RequestOptions().placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
        )


}