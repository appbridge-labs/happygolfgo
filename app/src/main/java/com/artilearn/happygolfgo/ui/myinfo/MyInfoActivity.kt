package com.artilearn.happygolfgo.ui.myinfo

import android.content.Context
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
import com.artilearn.happygolfgo.enummodel.UploadProfileType
import com.artilearn.happygolfgo.util.MyApplication
import com.artilearn.happygolfgo.util.snackbar
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
                    myApplication.myinfoRepository
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
            emptyNickName.observe(this@MyInfoActivity, Observer {
                showEmptyNickName()
            })
            failNickName.observe(this@MyInfoActivity, Observer {
                showFailNickName()
            })
            localName.observe(this@MyInfoActivity, Observer { name ->
                loadName(name)
            })
            localNickName.observe(this@MyInfoActivity, Observer { nickname ->
                loadNickName(nickname)
            })
            upload.observe(this@MyInfoActivity, Observer {
                uploadType(it)
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

    private fun showEmptyNickName() {
        binding.etNickname.error = getString(R.string.myinfo_empty_nickname)
    }

    private fun showFailNickName() {
        binding.etNickname.error = getString(R.string.myinfo_fail_nickname)
    }

    private fun loadName(name: String) {
        binding.etName.setText(name)
    }

    private fun loadNickName(nickname: String?) {
        nickname?.let {
            binding.etNickname.hint = it
        }
    }

    private fun uploadType(type: UploadProfileType) {
        when (type) {
            UploadProfileType.SUCCESS -> parentLayout.snackbar("tjdrhd")
            UploadProfileType.NETWORK_FAIL -> parentLayout.snackbar("fail")
            UploadProfileType.UNKNOW_ERROR -> parentLayout.snackbar("error")
        }
        finish()
    }

    override fun bottomAction() {
        super.bottomAction()
        viewModel.onConfirm()
    }
}