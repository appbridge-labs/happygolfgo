package com.artilearn.happygolfgo.ui.home.record

import android.os.Bundle
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.base.BaseFragment
import com.artilearn.happygolfgo.constants.MY_GOLF_POWER_ADAPTER_INDEX
import com.artilearn.happygolfgo.databinding.FragmentGolfRankingInBranchBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class GolfRankingInBranchFragment :
    BaseFragment<FragmentGolfRankingInBranchBinding, GolfRankingInBranchViewModel>
    (R.layout.fragment_golf_ranking_in_branch){
    private var pageIndex = -1
    override val viewModel: GolfRankingInBranchViewModel by viewModel()
    private lateinit var  adapter: GolfRankingInBranchAdapter

    companion object {
        fun newInstance(pageIndex: Int): GolfRankingInBranchFragment {
            val args = Bundle()
            args.putInt(MY_GOLF_POWER_ADAPTER_INDEX, pageIndex)
            val fragment = GolfRankingInBranchFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun init() {
        initBinding()
        initAdapter()
        val pageIndex = arguments?.getInt(MY_GOLF_POWER_ADAPTER_INDEX)
        pageIndex?.let {
            this.pageIndex = pageIndex
        }
        initViewModelObserving()
        initRequest()
    }

    private fun initBinding() {
        binding.vm = viewModel
    }

    private fun initViewModelObserving() {
// items of list adapter is directtly being binded in xml through adapter
//        with(viewModel) {
//
//        }
    }

    private fun initAdapter() {
        adapter = GolfRankingInBranchAdapter(viewModel)
        binding.recyclerview.adapter = adapter
    }

    private fun initRequest() {
        viewModel.pageIndex = pageIndex
        viewModel.request()
    }
}