package com.artilearn.happygolfgo.ui.gamerank.allgroup

import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.base.BaseFragment
import com.artilearn.happygolfgo.databinding.FragmentAllgroupBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class AllGroupFragment : BaseFragment<FragmentAllgroupBinding, AllGroupViewModel>(R.layout.fragment_allgroup) {

    override val viewModel: AllGroupViewModel by viewModel()

    override fun init() {

    }
}