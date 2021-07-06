package com.scz.cointracker.view

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.scz.cointracker.R
import com.scz.cointracker.databinding.BottomSheetBinding
import com.scz.cointracker.room.Coin
import com.scz.cointracker.viewmodel.BottomSheetVieModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BottomSheet : BottomSheetDialogFragment() {

    private lateinit var binding: BottomSheetBinding
    lateinit var viewModel: BottomSheetVieModel
    private lateinit var mListener: BottomSheetDismissListener
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.bottom_sheet, container, false)
        return binding.root
    }

    @SuppressLint("ShowToast")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViewModel()
        binding.save.setOnClickListener {
            viewModel.saveCoin(
                Coin(
                    binding.recordName.text.toString(),
                    binding.coinSymbol.text.toString(),
                    binding.buyedPrice.text.toString().toDouble(),
                    binding.buyedUnit.text.toString().toDouble()
                )
            )
            dismiss()
            mListener.onDismiss()
        }
    }

    fun setDismissListener(listener: BottomSheetDismissListener) {
        mListener = listener
    }

    private fun bindViewModel() {
        viewModel = ViewModelProvider(requireActivity()).get(BottomSheetVieModel::class.java)
    }

    interface BottomSheetDismissListener {
        fun onDismiss()
    }
}