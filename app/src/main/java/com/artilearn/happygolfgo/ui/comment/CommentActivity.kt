package com.artilearn.happygolfgo.ui.comment

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.base.BaseActivity
import com.artilearn.happygolfgo.constants.LESSON_LOG
import com.artilearn.happygolfgo.data.LessonLog
import com.artilearn.happygolfgo.databinding.ActivityCommetBinding
import com.artilearn.happygolfgo.enummodel.BottimViewType
import com.artilearn.happygolfgo.enummodel.ToastType
import com.artilearn.happygolfgo.ui.log.LogActivity
import com.artilearn.happygolfgo.ui.myinfo.MyInfoActivity
import com.artilearn.happygolfgo.ui.mypage.MyPageActivity
import com.artilearn.happygolfgo.util.MyApplication
import com.artilearn.happygolfgo.util.snackbar
import kotlinx.android.synthetic.main.custom_bottom_view.view.*

class CommentActivity : BaseActivity<ActivityCommetBinding>(R.layout.activity_commet) {

    private val lessonLog by lazy { intent.getParcelableExtra<LessonLog>(LESSON_LOG) }
    private lateinit var myApplication: MyApplication
    private val viewModel: CommentViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return CommentViewModel(
                    myApplication.commentRepository
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
        binding.log = lessonLog
        viewModel.lessonId = lessonLog.id
    }

    private fun initViewModelObserve() {
        with(viewModel) {
            emptyRating.observe(this@CommentActivity, Observer {
                showEmptyRating()
            })
            emptyComment.observe(this@CommentActivity, Observer {
                showEmptyComment()
            })
            networkFail.observe(this@CommentActivity, Observer {
                showToast(ToastType.WARNNING, getString(R.string.fragment_network_fail_content))
            })
            serverError.observe(this@CommentActivity, Observer { errorMsg ->
                showToast(ToastType.ERROR, getString(R.string.comment_upload_fail)+"\n$errorMsg")
            })
            success.observe(this@CommentActivity, Observer {
                goMyPage()
            })
        }
    }

    private fun initView() {
        initToolbar(binding.commentToolbar.toolBar, getString(R.string.log_toolbar_title), true)
        initBottomView(binding.commentBottomView.bottomLayout, getString(R.string.bottom_view_default), BottimViewType.DEFAULT_TYPE)
    }

    private fun showEmptyRating() {
        binding.commentLayout.snackbar(getString(R.string.comment_empty_rating))
    }

    private fun showEmptyComment() {
        binding.commentLayout.snackbar(getString(R.string.comment_empty_comment))
    }

    private fun goMyPage() {
        showToast(ToastType.SUCCESS, getString(R.string.comment_upload_success))
        val myPageIntent = Intent(this, MyPageActivity::class.java)
            .apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
        startActivity(myPageIntent)
    }

    override fun bottomAction() {
        super.bottomAction()
        viewModel.uploadLessonCommet()
    }

}