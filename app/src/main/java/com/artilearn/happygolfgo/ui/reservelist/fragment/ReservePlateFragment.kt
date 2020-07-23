package com.artilearn.happygolfgo.ui.reservelist.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.base.BaseFragment
import com.artilearn.happygolfgo.databinding.FragmentReservePlateBinding

class ReservePlateFragment
    : BaseFragment<FragmentReservePlateBinding, ReservePlateViewModel>(R.layout.fragment_reserve_plate) {
    private val TAG = javaClass.simpleName

    companion object {

        fun newInstance(): ReservePlateFragment {
            val args = Bundle()
            val fragment = ReservePlateFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override val viewModel: ReservePlateViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return ReservePlateViewModel() as T
            }
        }
    }

    override fun init() {
        Log.d(TAG, "init: ")
    }
}