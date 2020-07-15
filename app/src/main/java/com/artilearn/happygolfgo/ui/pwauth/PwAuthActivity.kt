package com.artilearn.happygolfgo.ui.pwauth

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.base.BaseActivity
import com.artilearn.happygolfgo.databinding.ActivityPwAuthBinding
import com.artilearn.happygolfgo.enummodel.BottimViewType
import com.artilearn.happygolfgo.enummodel.PwAuthPhoneType
import com.artilearn.happygolfgo.util.MyApplication
import com.artilearn.happygolfgo.util.snackbar
import kotlinx.android.synthetic.main.activity_pw_auth.*
import kotlinx.android.synthetic.main.custom_bottom_view.view.*
import kotlinx.android.synthetic.main.custom_toolbar.view.*

class PwAuthActivity : BaseActivity<ActivityPwAuthBinding>(R.layout.activity_pw_auth) {

    private lateinit var myApplication: MyApplication
    private val viewModel: PwAuthViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return PwAuthViewModel(

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
            phoneNumber.observe(this@PwAuthActivity, Observer {
                phoneType(it)
            })
        }
    }

    private fun initView() {
        initToolbar(pwauth_toolbar.toolBar, getString(R.string.pwauth_toolbar_title), true)
        initBottomView(
            pwauth_bottom_view.bottomLayout,
            getString(R.string.pwauth_bottom_view),
            BottimViewType.DEFAULT_TYPE
        )
    }

    private fun phoneType(type: PwAuthPhoneType) {
        when (type) {
            PwAuthPhoneType.EMPTY -> binding.etPhone.error = getString(R.string.pwauth_empty_phone)
            PwAuthPhoneType.FAIL -> binding.etPhone.error = getString(R.string.pwauth_fail_phone)
            PwAuthPhoneType.SUCCESS -> parentLayout.snackbar(getString(R.string.pwauth_success_phone))
        }
    }
}