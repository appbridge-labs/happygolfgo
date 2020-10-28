package com.artilearn.happygolfgo.ui.home.alram

import androidx.lifecycle.Observer
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.base.BaseFragment
import com.artilearn.happygolfgo.databinding.FragmentAlramBinding
import com.artilearn.happygolfgo.enummodel.ToastType
import org.koin.androidx.viewmodel.ext.android.viewModel

class AlramFragment
    : BaseFragment<FragmentAlramBinding, AlramViewModel>(R.layout.fragment_alram) {

    override val viewModel: AlramViewModel by viewModel()
    private lateinit var adapter: AlramAdapter

    override fun init() {

        initBinding()
        initViewModelObserving()
        initAdapter()
    }

    private fun initBinding() {
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