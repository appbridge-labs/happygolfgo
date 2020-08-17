package com.artilearn.happygolfgo.ui.pwreset

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.base.BaseActivity
import com.artilearn.happygolfgo.constants.PHONE_NUMBER
import com.artilearn.happygolfgo.databinding.ActivityPwResetBinding
import com.artilearn.happygolfgo.enummodel.BottimViewType
import com.artilearn.happygolfgo.enummodel.ToastType
import com.artilearn.happygolfgo.util.MyApplication
import com.artilearn.happygolfgo.util.snackbar
import io.reactivex.BackpressureStrategy
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.custom_bottom_view.view.*
import java.util.concurrent.TimeUnit

class PwResetActivity : BaseActivity<ActivityPwResetBinding>(R.layout.activity_pw_reset) {

    private val pattern = "^[A-Za-z0-9+]{8,20}\$"
    private lateinit var disposable: Disposable
    private var passwordStatus = false
    private var confirmStatus = false
    private lateinit var myApplication: MyApplication

    private val phone by lazy {
        intent.getStringExtra(PHONE_NUMBER)
    }

    private val viewModel: PwResetViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return PwResetViewModel(
                    myApplication.pwrestRepository
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
            networkFail.observe(this@PwResetActivity, Observer {
                showToast(ToastType.WARNNING, getString(R.string.fragment_network_fail_content))
            })
            serverError.observe(this@PwResetActivity, Observer {
                showToast(ToastType.ERROR, it)
            })
            changeSuccess.observe(this@PwResetActivity, Observer {
                showToast(ToastType.SUCCESS, getString(R.string.pwreset_change_success))
                finish()
            })
        }
    }

    private fun initView() {
        with(binding) {
            initToolbar(pwresetToolbar.toolbar, getString(R.string.pwreset_toolbar_title), true)
            initBottomView(pwresetBottomView.bottom_layout, getString(R.string.bottom_view_default), BottimViewType.DEFAULT_TYPE)
        }

        phone?.let { viewModel.currentPhone = it }
    }

    private fun createPassword(): Observable<String> {
        val text = Observable.create<String> { emitter ->
            val textWatcher = object : TextWatcher {
                override fun afterTextChanged(s: Editable?) = Unit

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    s?.toString()?.let { emitter.onNext(it) }
                }
            }

            binding.etPw.addTextChangedListener(textWatcher)

            emitter.setCancellable {
                binding.etPw.removeTextChangedListener(textWatcher)
            }
        }

        return text
            .debounce(500, TimeUnit.MILLISECONDS)
    }

    private fun createConfirmPassword(): Observable<String> {
        val text = Observable.create<String> { emitter ->
            val textWatcher = object : TextWatcher {
                override fun afterTextChanged(s: Editable?) = Unit

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    s?.toString()?.let { emitter.onNext(it) }
                }
            }

            binding.etPwConfirm.addTextChangedListener(textWatcher)

            emitter.setCancellable {
                binding.etPwConfirm.removeTextChangedListener(textWatcher)
            }
        }

        return text
            .debounce(500, TimeUnit.MILLISECONDS)
    }

    private fun rxPassword() {
        val textPassword = createPassword()
            .toFlowable(BackpressureStrategy.BUFFER)

        disposable = textPassword
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ password ->
                with(password) {
                    passwordStatus = when {
                        this.matches(pattern.toRegex()) -> {
                            passwordStatus(true)
                            true
                        }
                        else -> {
                            passwordStatus(false)
                            showErrorPassword()
                            false
                        }
                    }
                }
            }, {})
    }

    private fun rxConfirmPassword() {
        val textConfirmPassword = createConfirmPassword()
            .toFlowable(BackpressureStrategy.BUFFER)

        disposable = textConfirmPassword
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                with(it) {
                    if (this == binding.etPw.text.toString()) {
                        passwordConfirmStatus(true)
                        confirmStatus = true
                    } else {
                        confirmStatus = false
                        passwordConfirmStatus(false)
                        showErrorConfirmPassword()
                    }
                }
            }, {})
    }

    private fun showErrorPassword() {
        binding.etPw.error = getString(R.string.pwreset_input_fail_password)
    }

    private fun showErrorConfirmPassword() {
        binding.etPwConfirm.error = getString(R.string.pwreset_fail_password)
    }

    private fun passwordStatus(visible: Boolean) {
        binding.ivPwSuccess.visibility = if (visible) {
            View.VISIBLE
        } else {
            View.INVISIBLE
        }
    }

    private fun passwordConfirmStatus(visible: Boolean) {
        binding.ivPwConfirmSuccess.visibility = if (visible) {
            View.VISIBLE
        } else {
            View.INVISIBLE
        }
    }

    override fun bottomAction() {
        super.bottomAction()
        if (passwordStatus && confirmStatus) {
            viewModel.updatePassword()
        } else {
            binding.parentLayout.snackbar(getString(R.string.pwreset_please_check_pw))
            return
        }
    }

    override fun onStart() {
        super.onStart()
        rxPassword()
        rxConfirmPassword()
    }

    override fun onPause() {
        super.onPause()
        disposable.dispose()
    }
}