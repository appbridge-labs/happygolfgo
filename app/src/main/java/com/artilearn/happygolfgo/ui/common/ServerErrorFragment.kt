package com.artilearn.happygolfgo.ui.common

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.base.BaseFragment
import com.artilearn.happygolfgo.databinding.FragmentServerErrorBinding

class ServerErrorFragment
    : BaseFragment<FragmentServerErrorBinding, ServerErrorViewModel>(R.layout.fragment_server_error) {


    override val viewModel: ServerErrorViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return ServerErrorViewModel() as T
            }
        }
    }

    override fun init() {
        binding.vm = viewModel
    }
}