package com.artilearn.happygolfgo.ui.pwauth

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.base.BaseActivity
import com.artilearn.happygolfgo.databinding.ActivityPwAuthBinding
import com.artilearn.happygolfgo.enummodel.BottimViewType
import com.artilearn.happygolfgo.enummodel.PwAuthNumberType
import com.artilearn.happygolfgo.enummodel.PwAuthPhoneType
import com.artilearn.happygolfgo.enummodel.ToastType
import com.artilearn.happygolfgo.ui.pwreset.PwResetActivity
import com.artilearn.happygolfgo.util.MyApplication
import com.artilearn.happygolfgo.util.snackbar
import kotlinx.android.synthetic.main.activity_pw_auth.*
import kotlinx.android.synthetic.main.custom_bottom_view.view.*
import kotlinx.android.synthetic.main.custom_toolbar.view.*

class PwAuthActivity : BaseActivity<ActivityPwAuthBinding>(R.layout.activity_pw_auth) {

    private var status = false
    private lateinit var myApplication: MyApplication
    private val viewModel: PwAuthViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return PwAuthViewModel(
                    myApplication.pwAuthRepository
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
            authNumber.observe(this@PwAuthActivity, Observer {
                authType(it)
            })
            success.observe(this@PwAuthActivity, Observer {
                status = it
            })
        }
    }

    private fun initView() {
        initToolbar(binding.pwauthToolbar.toolbar, getString(R.string.pwauth_toolbar_title), true)
        initBottomView(binding.pwauthBottomView.bottom_layout, getString(R.string.bottom_view_default), BottimViewType.DEFAULT_TYPE)
    }

    private fun phoneType(type: PwAuthPhoneType) {
        when (type) {
            PwAuthPhoneType.EMPTY -> binding.etPhone.error = getString(R.string.pwauth_empty_phone)
            PwAuthPhoneType.LEGNTH_FAIL -> binding.etPhone.error = getString(R.string.pwauth_fail_length)
            PwAuthPhoneType.SUCCESS -> showToast(ToastType.SUCCESS, getString(R.string.pwauth_success_phone))
            PwAuthPhoneType.FAIL -> showToast(ToastType.ERROR, getString(R.string.pwauth_fail_phone))
            PwAuthPhoneType.EMPTY_USER -> showToast(ToastType.WARNNING, getString(R.string.pwauth_empty_user))
            PwAuthPhoneType.NETWORK_FAIL -> showToast(ToastType.WARNNING, getString(R.string.pwauth_network_fail))
        }
    }

    private fun authType(type: PwAuthNumberType) {
        when (type) {
            PwAuthNumberType.EMPTY -> binding.etAuth.error = getString(R.string.pwauth_empty_auth)
            PwAuthNumberType.FAIL -> showToast(ToastType.ERROR, getString(R.string.pwauth_error_auth))
            PwAuthNumberType.NETWORK_FAIL -> showToast(ToastType.WARNNING, getString(R.string.pwauth_network_fail))
            PwAuthNumberType.SUCCESS -> showToast(ToastType.SUCCESS, getString(R.string.pwauth_success_auth))
            PwAuthNumberType.UNCREATE -> showToast(ToastType.WARNNING, getString(R.string.pwauth_uncreate_auth))
            PwAuthNumberType.AUTH_ERROR -> showToast(ToastType.ERROR, getString(R.string.pwauth_fail_auth))
        }
    }

    private fun goPwReset() {
        startActivity(Intent(this, PwResetActivity::class.java))
        finish()
    }

    override fun bottomAction() {
        super.bottomAction()
        if (status) {
            goPwReset()
        }
    }
}