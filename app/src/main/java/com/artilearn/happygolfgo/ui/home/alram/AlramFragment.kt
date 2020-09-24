package com.artilearn.happygolfgo.ui.home.alram

import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.base.BaseFragment
import com.artilearn.happygolfgo.databinding.FragmentAlramBinding
import com.artilearn.happygolfgo.enummodel.ToastType
import com.artilearn.happygolfgo.util.MyApplication

class AlramFragment
    : BaseFragment<FragmentAlramBinding, AlramViewModel>(R.layout.fragment_alram) {

    private lateinit var adapter: AlramAdapter
    private lateinit var myApplication: MyApplication

    override val viewModel: AlramViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return AlramViewModel(
                    myApplication.alramRepository
                ) as T
            }
        }
    }

    override fun init() {

        inject()
        initViewModelObserving()
        initAdapter()
    }

    private fun inject() {
        myApplication = activity?.application as MyApplication
        binding.vm = viewModel
    }

    private fun initViewModelObserving() {
        with(viewModel) {
            serverError.observe(this@AlramFragment, Observer {
                showFragmentToast(ToastType.ERROR, it)
            })
            networkFail.observe(this@AlramFragment, Observer {
                showFragmentToast(
                    ToastType.WARNNING,
                    getString(R.string.fragment_network_fail_content)
                )
            })
        }
    }

    private fun initAdapter() {
        adapter = AlramAdapter { alram ->
            when (alram.eventType) {
                "3" -> ""
                "4" -> ""
                "6" -> ""
            }
        }

        binding.recyclerview.adapter = adapter
    }
}