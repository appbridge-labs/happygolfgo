package com.artilearn.happygolfgo.ui.gamerank.mygroup

import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.base.BaseFragment
import com.artilearn.happygolfgo.databinding.FragmentMygroupBinding
import com.artilearn.happygolfgo.ui.gamerank.GameRankViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MyGroupFragment : BaseFragment<FragmentMygroupBinding, GameRankViewModel>(R.layout.fragment_mygroup) {

    override val viewModel: GameRankViewModel by sharedViewModel()

    override fun init() {

    }
}