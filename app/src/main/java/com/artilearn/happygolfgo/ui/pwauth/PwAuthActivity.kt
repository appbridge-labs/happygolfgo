package com.artilearn.happygolfgo.ui.pwauth

import android.os.Bundle
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.base.BaseActivity
import com.artilearn.happygolfgo.databinding.ActivityPwAuthBinding
import com.artilearn.happygolfgo.enummodel.BottimViewType
import com.artilearn.happygolfgo.enummodel.PwAuthNumberType
import com.artilearn.happygolfgo.enummodel.PwAuthPhoneType
import com.artilearn.happygolfgo.enummodel.ToastType
import kotlinx.android.synthetic.main.custom_bottom_view.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class PwAuthActivity : BaseActivity<ActivityPwAuthBinding>(R.layout.activity_pw_auth) {

    private val viewModel: PwAuthViewModel by viewModel()
    private var status = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initBinding()
        initViewModelObserve()
        initView()
        initTextListener()
    }

    private fun initBinding() {
        binding.vm = viewModel
    }

    private fun initViewModelObserve() {
        with(viewModel) {
            phoneValueType.observe(this@PwAuthActivity, Observer { phoneType ->
                phoneType(phoneType)
            })
            authValueType.observe(this@PwAuthActivity, Observer { authType ->
                authType(authType)
            })
        }
    }

    private fun initView() {
        initToolbar(binding.pwauthToolbar.toolbar, getString(R.string.pwauth_toolbar_title), true)
        initBottomView(binding.pwauthBottomView.bottom_layout, getString(R.string.bottom_view_default), BottimViewType.DEFAULT_TYPE)
    }

    private fun initTextListener() {
        with(binding) {
            etPhone.addTextChangedListener { viewModel.phoneOnNext(it.toString()) }
            etAuth.addTextChangedListener { viewModel.authOnNext(it.toString()) }
        }
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
            PwAuthNumberType.UNCREATE -> showToast(ToastType.WARNNING, getString(R.string.pwauth_uncreate_auth))
            PwAuthNumberType.AUTH_ERROR -> showToast(ToastType.ERROR, getString(R.string.pwauth_fail_auth))
            PwAuthNumberType.MODEL_FAIL -> showToast(ToastType.ERROR, getString(R.string.pwauth_model_fail))
            PwAuthNumberType.TIMER_OVER -> showToast(ToastType.ERROR, getString(R.string.pwauth_timer_over))
            PwAuthNumberType.SUCCESS -> {
                showToast(ToastType.SUCCESS, getString(R.string.pwauth_success_auth))
                status = true
            }
        }
    }

    override fun bottomAction() {
        super.bottomAction()
        if (!status) {
            showToast(ToastType.WARNNING, "인증을 완료해 주세요")
        }
    }
}