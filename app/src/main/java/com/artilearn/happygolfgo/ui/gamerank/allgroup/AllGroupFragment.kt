package com.artilearn.happygolfgo.ui.gamerank.allgroup

import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.base.BaseFragment
import com.artilearn.happygolfgo.databinding.FragmentAllgroupBinding
import com.artilearn.happygolfgo.ui.gamerank.GameRankViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class AllGroupFragment : BaseFragment<FragmentAllgroupBinding, GameRankViewModel>(R.layout.fragment_allgroup) {

    override val viewModel: GameRankViewModel by sharedViewModel()

    override fun init() {

    }
}