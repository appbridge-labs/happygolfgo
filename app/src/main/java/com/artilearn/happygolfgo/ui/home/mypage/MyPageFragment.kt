package com.artilearn.happygolfgo.ui.home.mypage

import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.base.BaseFragment
import com.artilearn.happygolfgo.databinding.FragmentMypageBinding
import com.artilearn.happygolfgo.util.MyApplication
import kotlinx.android.synthetic.main.common_view.view.*

class MyPageFragment : BaseFragment<FragmentMypageBinding, MyPageViewModel>(R.layout.fragment_mypage) {

    private lateinit var myApplication: MyApplication

    override val viewModel: MyPageViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return MyPageViewModel() as T
            }
        }
    }
    override fun init() {

        inject()
        initView()
        initClickListener()
    }

    private fun inject() {
        myApplication = activity?.application as MyApplication
        binding.vm = viewModel
    }

    private fun initView() {
        with(binding) {
            includeInfor.tv_text.text = getString(R.string.mypage_lesson_title)
            includeRecord.tv_text.text = getString(R.string.mypage_info_change)
            includePassword.tv_text.text = getString(R.string.mypage_password_change)
            includeLogout.tv_text.text = getString(R.string.mypage_logout)
            includeUpdate.tv_text.text = getString(R.string.mypage_update)
        }
    }

    private fun initClickListener() {
        with(binding) {
            includeUpdate.setOnClickListener {  }
            includeInfor.setOnClickListener {  }
            includeRecord.setOnClickListener {  }
            includeLogout.setOnClickListener {  }
            includePassword.setOnClickListener {  }
        }
    }
}