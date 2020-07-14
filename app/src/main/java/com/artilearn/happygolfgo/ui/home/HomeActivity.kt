package com.artilearn.happygolfgo.ui.home

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.base.BaseActivity
import com.artilearn.happygolfgo.databinding.ActivityHomeBinding
import com.artilearn.happygolfgo.enummodel.BottimViewType
import com.artilearn.happygolfgo.enummodel.CommonFragmentType
import com.artilearn.happygolfgo.ui.mypage.MyPageActivity
import com.artilearn.happygolfgo.util.MyApplication

class HomeActivity : BaseActivity<ActivityHomeBinding>(R.layout.activity_home) {
    private val TAG = javaClass.simpleName

    private lateinit var myApplication: MyApplication
    private val viewModel: HomeViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return HomeViewModel(
                    myApplication.homeRepository
                ) as T
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        inject()
        initViewModelObserve()
        initView()
    }

    private fun inject() {
        myApplication = application as MyApplication
        binding.vm = viewModel
    }

    private fun initViewModelObserve() {
        with(viewModel) {
            myPage.observe(this@HomeActivity, Observer {
                goMyPage()
            })
        }
    }

    private fun initView() {
        initBottomView(binding.includeBottomView, getString(R.string.home_bottomview_title), BottimViewType.DEFAULT_TYPE)
        showCommonFragment(R.id.homeFrameLayout, null, CommonFragmentType.HOME)
    }

    private fun goMyPage() {
        startActivity(Intent(this, MyPageActivity::class.java))
    }

    override fun bottomAction() {
        super.bottomAction()
    }


}