package com.artilearn.happygolfgo.ui.pwauth

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.base.BaseActivity
import com.artilearn.happygolfgo.constants.PHONE_NUMBER
import com.artilearn.happygolfgo.databinding.ActivityPwAuthBinding
import com.artilearn.happygolfgo.enummodel.BottimViewType
import com.artilearn.happygolfgo.enummodel.PwAuthNumberType
import com.artilearn.happygolfgo.enummodel.PwAuthPhoneType
import com.artilearn.happygolfgo.enummodel.ToastType
import com.artilearn.happygolfgo.ui.pwreset.PwResetActivity
import com.artilearn.happygolfgo.util.MyApplication
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.custom_bottom_view.view.*
import java.util.concurrent.TimeUnit

class PwAuthActivity : BaseActivity<ActivityPwAuthBinding>(R.layout.activity_pw_auth) {

    private val phonePublishSubject = PublishSubject.create<String>()
    private val authPulishSubject = PublishSubject.create<String>()
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
        rxPhoneNumber()
        rxAuthNumber()
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
                rxDelayGoPwReset()
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
            PwAuthNumberType.MODEL_FAIL -> showToast(ToastType.ERROR, getString(R.string.pwauth_model_fail))
        }
    }

    private fun rxPhoneNumber() {
        binding.etPhone.doOnTextChanged { phone, _, _, _ ->
            phonePublishSubject.onNext(phone.toString())
        }
        phonePublishSubject.observeOn(AndroidSchedulers.mainThread())
            .subscribe { phone ->
                viewModel.currentPhone = phone
            }.addTo(compositeDisposable)
    }

    private fun rxAuthNumber() {
        binding.etAuth.doOnTextChanged { auth, _, _, _ ->
            authPulishSubject.onNext(auth.toString())
        }
        authPulishSubject.observeOn(AndroidSchedulers.mainThread())
            .subscribe { auth ->
                viewModel.currentAuth = auth
            }.addTo(compositeDisposable)
    }

    private fun rxDelayGoPwReset() {
        compositeDisposable.add(
            Completable.create { emitter ->
                emitter.onComplete()
            }
                .delay(500L, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    goPwReset()
                }
        )
    }

    private fun goPwReset() {
        val resetIntent = Intent(this, PwResetActivity::class.java)
            .apply {
                putExtra(PHONE_NUMBER, viewModel.currentPhone)
            }
        startActivity(resetIntent)
        finish()
    }

    override fun bottomAction() {
        super.bottomAction()
        if (status) {
            goPwReset()
        }
    }
}