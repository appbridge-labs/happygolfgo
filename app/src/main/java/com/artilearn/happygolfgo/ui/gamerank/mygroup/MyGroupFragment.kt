package com.artilearn.happygolfgo.ui.gamerank.mygroup

import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.base.BaseFragment
import com.artilearn.happygolfgo.databinding.FragmentMygroupBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class MyGroupFragment : BaseFragment<FragmentMygroupBinding, MyGroupViewModel>(R.layout.fragment_mygroup) {

    override val viewModel: MyGroupViewModel by viewModel()

    override fun init() {

    }
}