package com.artilearn.happygolfgo.ui.common

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.databinding.FragmentDeveloperOptionBinding
import com.artilearn.happygolfgo.util.MyApplication
import com.artilearn.happygolfgo.util.PreferenceManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class DeveloperOptionFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentDeveloperOptionBinding
    private lateinit var myApplication: MyApplication
    private val viewModel: DeveloperOptionViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return DeveloperOptionViewModel(
                    myApplication.developerOptionRepository
                ) as T
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_developer_option,
            container,
            false
        )

        inject()
        initViewModelCallback()

        return binding.root
    }

    private fun inject() {
        myApplication = activity?.applicationContext as MyApplication
        binding.vm = viewModel
        binding.lifecycleOwner = this
    }

    private fun initViewModelCallback() {
        with(viewModel) {
            prodution.observe(this@DeveloperOptionFragment, Observer {
                Toast.makeText(activity, PreferenceManager(MyApplication.getApplicationContext()).environmentMode, Toast.LENGTH_SHORT).show()
                closeDialog()
            })
            test.observe(this@DeveloperOptionFragment, Observer {
                Toast.makeText(activity, PreferenceManager(MyApplication.getApplicationContext()).environmentMode, Toast.LENGTH_SHORT).show()
                closeDialog()
            })
            developer.observe(this@DeveloperOptionFragment, Observer {
                Toast.makeText(activity, PreferenceManager(MyApplication.getApplicationContext()).environmentMode, Toast.LENGTH_SHORT).show()
                closeDialog()
            })
        }
    }

    private fun closeDialog() {
        dismiss()
    }

}