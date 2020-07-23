package com.artilearn.happygolfgo.ui.reservelist.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.base.BaseFragment
import com.artilearn.happygolfgo.databinding.FragmentReserveLessonBinding


class ReserveLessonFragment
    : BaseFragment<FragmentReserveLessonBinding, ReserveLessonViewModel>(R.layout.fragment_reserve_lesson) {
    private val TAG = javaClass.simpleName

    companion object {

        fun newInstance(): ReserveLessonFragment {
            val args = Bundle()
            val fragment = ReserveLessonFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override val viewModel: ReserveLessonViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return ReserveLessonViewModel() as T
            }
        }
    }

    override fun init() {
        Log.d(TAG, "init: ")
    }
}