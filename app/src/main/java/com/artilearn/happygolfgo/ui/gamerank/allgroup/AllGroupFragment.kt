package com.artilearn.happygolfgo.ui.gamerank.allgroup

import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.base.BaseFragment
import com.artilearn.happygolfgo.databinding.FragmentAllgroupBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class AllGroupFragment : BaseFragment<FragmentAllgroupBinding, AllGroupViewModel>(R.layout.fragment_allgroup) {

    override val viewModel: AllGroupViewModel by viewModel()

    //    override val viewModel: AllGroupViewModel by viewModels {
//        object : ViewModelProvider.Factory {
//            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
//                return AllGroupViewModel() as T
//            }
//        }
//    }

    override fun init() {

    }
}