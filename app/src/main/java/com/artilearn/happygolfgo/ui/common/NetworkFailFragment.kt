package com.artilearn.happygolfgo.ui.common

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.base.BaseFragment
import com.artilearn.happygolfgo.databinding.FragmentNetworkFailBinding
import com.artilearn.happygolfgo.ui.home.HomeFragment

class NetworkFailFragment
    : BaseFragment<FragmentNetworkFailBinding, NetworkFailViewModel>(R.layout.fragment_network_fail) {

    companion object {

        fun newInstance(): NetworkFailFragment {
            val args = Bundle()
            val fragment = NetworkFailFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override val viewModel: NetworkFailViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return NetworkFailViewModel() as T
            }
        }
    }

    override fun init() {
        binding.vm = viewModel

        initViewModelObserve()
    }

    private fun initViewModelObserve() {
        with(viewModel) {
            btnRetry.observe(this@NetworkFailFragment, Observer {
                showHomeView()
            })
        }
    }

    private fun showHomeView() {
        val home = HomeFragment.newInstance()
        activity?.supportFragmentManager!!.beginTransaction()
            .replace(R.id.homeFrameLayout, home)
            .commit()
    }
}