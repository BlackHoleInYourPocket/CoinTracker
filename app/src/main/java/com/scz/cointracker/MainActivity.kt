package com.scz.cointracker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentFactory
import com.scz.cointracker.view.CoinFragmentFactory
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var fragmentFactory: CoinFragmentFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportFragmentManager.fragmentFactory = fragmentFactory
        setContentView(R.layout.activity_main)
    }
}