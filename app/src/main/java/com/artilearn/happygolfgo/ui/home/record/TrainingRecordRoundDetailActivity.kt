package com.artilearn.happygolfgo.ui.home.record

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.base.BaseActivity
import com.artilearn.happygolfgo.constants.COACH_NAME
import com.artilearn.happygolfgo.constants.LESSON_DATE
import com.artilearn.happygolfgo.constants.LESSON_ID
import com.artilearn.happygolfgo.constants.SWING_ROUND_DETAIL_ID
import com.artilearn.happygolfgo.data.LogDetail
import com.artilearn.happygolfgo.databinding.ActivityLogBinding
import com.artilearn.happygolfgo.databinding.ActivityTrainingRecordRoundDetailBinding
import com.artilearn.happygolfgo.enummodel.BottimViewType
import com.artilearn.happygolfgo.enummodel.ToastType
import com.artilearn.happygolfgo.ui.comment.CommentActivity
import com.artilearn.happygolfgo.ui.log.LogViewModel
import com.artilearn.happygolfgo.util.convertTime
import com.artilearn.happygolfgo.util.setImage
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import kotlinx.android.synthetic.main.common_default_view.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class TrainingRecordRoundDetailActivity :
    BaseActivity<ActivityTrainingRecordRoundDetailBinding>(R.layout.activity_training_record_round_detail)
{
    private val viewModel: TrainingRecordRoundDetailViewModel by viewModel()
    private val gmID by lazy { intent.getIntExtra(SWING_ROUND_DETAIL_ID, 0) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding()
        initViewModelObserve()
        initView()
    }

    private fun initBinding() {
        binding.vm = viewModel
        viewModel.requestRoundDetail(gmID)
    }

    private fun initViewModelObserve() {
        with(viewModel) {
//            logDetail.observe(this@LogActivity, Observer {
//                binding.log = it
//                setLessonLogItem(it)
//                setLessonLogText(it)
//
//                log = it
//                log?.videoURL?.let { path ->
//                    initializePlayer(path)
//                }
//            })
//            videoUrl.observe(this@LogActivity, Observer { url ->
//                showVideoView()
//            })

            roundDetail.observe(this@TrainingRecordRoundDetailActivity, Observer { roundDetail->
                binding.roundDetail = roundDetail;
            })

            serverError.observe(this@TrainingRecordRoundDetailActivity, Observer { errorMsg ->
                showToast(ToastType.ERROR, errorMsg)
            })
            networkFail.observe(this@TrainingRecordRoundDetailActivity, Observer {
                showNetworkFail()
            })
            appRefresh.observe(this@TrainingRecordRoundDetailActivity, Observer {
                goRefresh()
            })
        }
    }

    private fun initView() {
        initToolbar(binding.detailToolbar.toolbar, getString(R.string.training_record_score_card_toolbar_title), true)
    }

    private fun convertLessonLogTime(logCreateTime: String): String {
        val calendar = convertTime(logCreateTime).trim().split(":")

        return "${calendar[0]}.${calendar[1]}.${calendar[2]}"
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onStop() {
        super.onStop()
    }

}