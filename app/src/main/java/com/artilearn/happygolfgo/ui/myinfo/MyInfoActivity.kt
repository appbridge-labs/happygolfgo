package com.artilearn.happygolfgo.ui.myinfo

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.base.BaseActivity
import com.artilearn.happygolfgo.databinding.ActivityMyInfoBinding
import com.artilearn.happygolfgo.enummodel.BottimViewType
import com.artilearn.happygolfgo.util.MyApplication
import kotlinx.android.synthetic.main.activity_my_info.*
import kotlinx.android.synthetic.main.custom_bottom_view.view.*
import kotlinx.android.synthetic.main.custom_toolbar.view.*

class MyInfoActivity : BaseActivity<ActivityMyInfoBinding>(R.layout.activity_my_info) {
    private val TAG = javaClass.simpleName

    private lateinit var myApplication: MyApplication
    private val viewModel: MyInfoViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return MyInfoViewModel(

                ) as T
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        inject()
        initViewModelObserve()
        initView()
        initClickListener()
    }

    private fun inject() {
        myApplication = application as MyApplication
        binding.vm = viewModel
    }

    private fun initViewModelObserve() {
        with(viewModel) {
            success.observe(this@MyInfoActivity, Observer {
                Log.d(TAG, "initViewModelObserve: success")
            })
            emptyNickName.observe(this@MyInfoActivity, Observer {
                showEmptyNickName()
            })
            failNickName.observe(this@MyInfoActivity, Observer {
                showFailNickName()
            })
        }
    }

    private fun initView() {
        initToolbar(myinfo_toolbar.toolBar, getString(R.string.myinfo_toolbar_title), true)
        initBottomView(
            myinfo_bottom_view.bottomLayout,
            getString(R.string.myinfo_bottom_title),
            BottimViewType.DEFAULT_TYPE
        )
    }

    private fun initClickListener() {
        binding.myinfoBottomView.setOnClickListener { viewModel.onConfirm() }
    }

    private fun showEmptyNickName() {
        binding.etNickname.error = getString(R.string.myinfo_empty_nickname)
    }

    private fun showFailNickName() {
        binding.etNickname.error = getString(R.string.myinfo_fail_nickname)
    }
}