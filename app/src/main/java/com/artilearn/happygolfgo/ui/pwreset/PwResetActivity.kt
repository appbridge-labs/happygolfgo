package com.artilearn.happygolfgo.ui.pwreset

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.base.BaseActivity
import com.artilearn.happygolfgo.databinding.ActivityPwResetBinding
import com.artilearn.happygolfgo.enummodel.BottimViewType
import com.artilearn.happygolfgo.util.MyApplication
import kotlinx.android.synthetic.main.activity_pw_reset.*
import kotlinx.android.synthetic.main.custom_bottom_view.view.*
import kotlinx.android.synthetic.main.custom_toolbar.view.*

class PwResetActivity : BaseActivity<ActivityPwResetBinding>(R.layout.activity_pw_reset) {

    private lateinit var myApplication: MyApplication
    private val viewModel: PwResetViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return PwResetViewModel(

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

        }
    }

    private fun initView() {
        initToolbar(pwreset_toolbar.toolBar, "비밀번호 재설정", true)
        initBottomView(pwreset_bottom_view.bottomLayout, "확인", BottimViewType.DEFAULT_TYPE)
    }
}