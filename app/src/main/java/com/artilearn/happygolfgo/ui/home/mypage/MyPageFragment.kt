package com.artilearn.happygolfgo.ui.home.mypage

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.base.BaseFragment
import com.artilearn.happygolfgo.constants.PLAY_STORE
import com.artilearn.happygolfgo.databinding.FragmentMypageBinding
import com.artilearn.happygolfgo.enummodel.ToastType
import com.artilearn.happygolfgo.ui.lessonpostlist.LessonPostListActivity
import com.artilearn.happygolfgo.ui.login.LoginActivity
import com.artilearn.happygolfgo.ui.myinfo.MyInfoActivity
import com.artilearn.happygolfgo.ui.pwauth.PwAuthActivity
import kotlinx.android.synthetic.main.common_view.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MyPageFragment
    : BaseFragment<FragmentMypageBinding, MyPageViewModel>(R.layout.fragment_mypage) {

    override val viewModel: MyPageViewModel by viewModel()

    override fun init() {

        initBinding()
        initViewModelObserving()
        initView()
        initClickListener()
    }

    private fun initBinding() {
        binding.vm = viewModel
    }

    private fun initViewModelObserving() {
        with(viewModel) {
            message.observe(this@MyPageFragment, Observer { message ->
                showFragmentToast(ToastType.WARNNING, message = message)
            })
            eventType.observe(this@MyPageFragment, Observer {
                when (eventType.value) {
                    MyPageViewModel.MyPageStateType.LOGOUT -> goLogin()
                    MyPageViewModel.MyPageStateType.NECESSARY_UPDATE -> hideUpdate()
                    MyPageViewModel.MyPageStateType.NEED_UPDATE -> showUpdate()
                    MyPageViewModel.MyPageStateType.PLAY_STORE -> goPlayStore()
                }
            })
            appVersion.observe(this@MyPageFragment, Observer { version ->
                binding.tvVersion.text = String.format("v $version")
            })
            networkFail.observe(this@MyPageFragment, Observer {
                showFragmentToast(ToastType.WARNNING, getString(R.string.fragment_network_fail_content))
            })
            appRefresh.observe(this@MyPageFragment, Observer {
                goRefreshFromFragment()
            })
        }
    }

    private fun initView() {
        with(binding) {
            includeInfor.tv_text.text = getString(R.string.mypage_info_change)
            includeRecord.tv_text.text = getString(R.string.mypage_lesson_title)
            includePassword.tv_text.text = getString(R.string.mypage_password_change)
            includeLogout.tv_text.text = getString(R.string.mypage_logout)
            //includeBranch.tv_text.text = getString(R.string.mypage_branch_change)
            includeUpdate.tv_text.text = getString(R.string.mypage_update)
            includeUpdate.tv_text.setTextColor(ContextCompat.getColor(includeUpdate.tv_text.context, R.color.color_mypage_update))
            includeUpdate.parent_card.setBackgroundResource(R.drawable.bg_mypage_update)
        }
    }

    private fun initClickListener() {
        with(binding) {
            includeUpdate.setOnClickListener { viewModel.appUpdateOnNext() }
            includeInfor.setOnClickListener { goMyInfo() }
            includeRecord.setOnClickListener { goLessonLog() }
            includeLogout.setOnClickListener { viewModel.logout() }
            includePassword.setOnClickListener { goPwAuth() }
//            includeBranch.setOnClickListener {
//                val dialog = Dialog(requireActivity())
//                dialog.setContentView(R.layout.dialog_training)
//                dialog.show()
//            }
        }
    }

    private fun showUpdate() {
        binding.updateLayout.visibility = View.VISIBLE
    }

    private fun hideUpdate() {
        binding.updateLayout.visibility = View.GONE
    }

    private fun goLogin() {
        val intent = Intent(activity, LoginActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
        activity?.finish()
    }

    private fun goPlayStore() {
        val playStore = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(PLAY_STORE)
            setPackage("com.android.vending")
        }
        startActivity(playStore)
    }

    private fun goMyInfo() {
        startActivity(Intent(activity, MyInfoActivity::class.java))
    }

    private fun goPwAuth() {
        startActivity(Intent(activity, PwAuthActivity::class.java))
    }

    private fun goLessonLog() {
        startActivity(Intent(activity, LessonPostListActivity::class.java))
    }

}