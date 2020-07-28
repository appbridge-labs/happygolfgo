package com.artilearn.happygolfgo.ui.log

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.base.BaseActivity
import com.artilearn.happygolfgo.constants.LESSON_LOG
import com.artilearn.happygolfgo.constants.VIDEO_URL
import com.artilearn.happygolfgo.data.LessonLog
import com.artilearn.happygolfgo.data.LogDetail
import com.artilearn.happygolfgo.databinding.ActivityLogBinding
import com.artilearn.happygolfgo.enummodel.BottimViewType
import com.artilearn.happygolfgo.ui.comment.CommentActivity
import com.artilearn.happygolfgo.ui.video.VideoActivity
import com.artilearn.happygolfgo.util.MyApplication
import com.artilearn.happygolfgo.util.convertTime
import com.artilearn.happygolfgo.util.setImage
import kotlinx.android.synthetic.main.activity_log.*
import kotlinx.android.synthetic.main.common_default_view.view.*
import kotlinx.android.synthetic.main.custom_toolbar.view.*

class LogActivity : BaseActivity<ActivityLogBinding>(R.layout.activity_log) {

    private val lessonLog by lazy { intent.getParcelableExtra<LessonLog>(LESSON_LOG) }
    private lateinit var myApplication: MyApplication
    private val viewModel: LogViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return LogViewModel(
                    myApplication.logRespository
                ) as T
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        inject()
        initViewModelObserve()
        initView()
        setLessonLogText()
    }

    private fun inject() {
        myApplication = application as MyApplication
        binding.vm = viewModel
    }

    private fun initViewModelObserve() {
        with(viewModel) {
            logDetail.observe(this@LogActivity, Observer {
                binding.log = it
                setLessonLogItem(it)
            })
            videoUrl.observe(this@LogActivity, Observer { url ->
                goVideo(url)
            })
            serverError.observe(this@LogActivity, Observer { errorMsg ->
                showError(errorMsg)
            })
            networkFail.observe(this@LogActivity, Observer {
                showNetworkFail()
            })
        }
    }

    private fun initView() {
        initToolbar(log_toolbar.toolBar, getString(R.string.log_toolbar_title), true)

        lessonLog?.id.let {
            viewModel.requestLessonLogDetail(it)
        }

    }

    private fun setLessonLogText() {
        with(binding) {
            logName.tv_common.text = lessonLog.employeeName
            logDate.tv_common.text = convertLessonLogTime()
        }
    }

    private fun convertLessonLogTime(): String {
        val calendar = convertTime(lessonLog.createdAt).trim().split(":")

        return "${calendar[0]}.${calendar[1]}.${calendar[2]}"
    }

    private fun setLessonLogItem(log: LogDetail) {
        with(log) {
            loadThumbNail(log.thumbnailURL, log.body)

            //todo isReviewed 서버에서 false로 옴 계속
            if (isReviewed) {
                setMyCommet(log.comment)
            } else {
                if (isReviewable) {
                    underIsReviewable()
                } else {
                    overIsReviewable()
                }
            }
        }
    }

    private fun loadThumbNail(thumbNail: String?, body: String) {
        if (thumbNail.isNullOrEmpty()) {
            binding.videoLayout.visibility = View.GONE
        } else {
            with(binding.ivThumbnail) {
                setImage(this, thumbNail)
            }
        }
        binding.tvBody.text = body
    }

    private fun setMyCommet(commet: String?) {
        commet?.let {
            with(binding) {
                logCommentTitle.tv_common.text = getString(R.string.log_comment_tile)
                tvComment.text = commet
                logBottomView.visibility = View.GONE
            }
        }
    }

    private fun overIsReviewable() {
        with(binding) {
            tvComment.visibility = View.GONE
            logBottomView.visibility = View.GONE
            logCommentTitle.tv_common.apply {
                setTextColor(ContextCompat.getColor(this.context, R.color.color_sub_gray))
                text = getString(R.string.log_over_comment)
            }
        }
    }

    private fun underIsReviewable() {
        with(binding) {
            initBottomView(logBottomView, getString(R.string.log_bottom_view_title), BottimViewType.DEFAULT_TYPE)
            logCommentTitle.visibility = View.GONE
            tvComment.visibility = View.GONE
        }
    }

    private fun goVideo(videoUrl: String?) {
        videoUrl?.let {
            val videoIntent = Intent(this, VideoActivity::class.java)
                .apply {
                    putExtra(VIDEO_URL, videoUrl)
                }
            startActivity(videoIntent)
        }
    }

    private fun goComment(lessonLog: LessonLog) {
        val commentIntent = Intent(this, CommentActivity::class.java)
            .apply {
                putExtra(LESSON_LOG, lessonLog)
            }
        startActivity(commentIntent)
    }

    override fun bottomAction() {
        super.bottomAction()
        goComment(lessonLog)
    }
}