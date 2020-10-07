package com.artilearn.happygolfgo.ui.gamerank.mygroup

import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.base.BaseFragment
import com.artilearn.happygolfgo.databinding.FragmentMygroupBinding

class MyGroupFragment : BaseFragment<FragmentMygroupBinding, MyGroupViewModel>(R.layout.fragment_mygroup) {

    override val viewModel: MyGroupViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return MyGroupViewModel() as T
            }
        }
    }

    override fun init() {

    }
}