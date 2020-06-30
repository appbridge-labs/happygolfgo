package com.artilearn.happygolfgo.ui.user

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.library.HappyGolfGoRequest
import com.artilearn.happygolfgo.library.HappyGolfGoUserManager
import com.artilearn.happygolfgo.models.LessonReviewDetail
import com.artilearn.happygolfgo.models.LessonReviewList
import com.artilearn.happygolfgo.ui.BaseActivity
import com.artilearn.happygolfgo.util.convertTime
import com.artilearn.happygolfgo.util.setImage
import com.google.gson.internal.LinkedTreeMap
import com.tlsolution.tlsmodules.Network.RequestManager
import com.tlsolution.tlsmodules.Network.RequestResult
import kotlinx.android.synthetic.main.activity_lesson_review.*
import kotlinx.android.synthetic.main.partial_bottom_button.view.*
import kotlinx.android.synthetic.main.partial_menu_with_right_arrow.view.*
import kotlinx.android.synthetic.main.partial_toolbar.view.*

class LessonReviewActivity : BaseActivity() {

    private lateinit var requestManager: RequestManager
    private lateinit var userManager: HappyGolfGoUserManager
    private lateinit var request: HappyGolfGoRequest
    private var lessonItem: LessonReviewList? = null
    private var lessonReviewDetail: LessonReviewDetail? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lesson_review)

        initView()
        initListener()
        requestLessonReviewDetail()
    }

    private fun initView() {
        requestManager = RequestManager(this)
        userManager = HappyGolfGoUserManager(this)

        lessonItem = intent.getSerializableExtra("lessonReview") as LessonReviewList

        bottom_nav.text_view_title_bottom_button.text = "후기 남기기"
        setUpActionBar(review_toolbar.toolbar, "레슨 일지", true)

        setFrameLayoutID(R.id.frameLayout)
        setBaseFragments()
    }

    private fun initListener() {
        iv_video_start.setOnClickListener {
            startVideo(lessonReviewDetail?.videoURL)
        }
        bottom_nav.setOnClickListener {
            goReviewWrite()
        }
    }

    private fun requestLessonReviewDetail() {
        showLoading()
        request = HappyGolfGoRequest.lessonReviewDetail
        request.endpoint = lessonItem?.id.toString()

        requestManager.request(request, userManager.user) { result, json ->
            when (result) {
                RequestResult.nSuccess -> {
                    runOnUiThread { hideLoading() }
                }
                RequestResult.pSuccess -> {
                    when (result.statusCode) {
                        200 -> {
                            val lessonJson = json?.get("lessonLog") as LinkedTreeMap<String, Any>
                            lessonReviewDetail = LessonReviewDetail.create(lessonJson)
                            lessonReviewDetail?.let {
                                loadItem(it)
                            }
                        }
                    }
                }
                RequestResult.failure -> {
                    runOnUiThread {
                        hideLoading()
                        showUnknownError()
                    }
                }
                else -> {
                    runOnUiThread {
                        hideLoading()
                        showUnknownError()
                    }
                }
            }
        }
    }

    private fun loadItem(item: LessonReviewDetail) {
        runOnUiThread {
            val date = convertTime(item.createdAt).trim().split(":")
            tv_coach_name.text = lessonItem?.employeeName
            tv_date.text = "${date[0]}.${date[1]}.${date[2]}"
            tv_coach_message.text = item.body

            if (item.thumbnailURL.isNullOrEmpty()) {
                thumbNailLayout.visibility = View.GONE
            } else {
                iv_thumbnail.setImage(iv_thumbnail, item.thumbnailURL)
            }

            if (item.isReviewed) {
                bottom_nav.visibility = View.GONE
                myCommentLayout.visibility = View.VISIBLE
                tv_my_review.text = item.comment
            } else {
                bottom_nav.visibility = View.VISIBLE
            }

            hideLoading()
        }
    }

    private fun goReviewWrite() {
        val intent = Intent(this, LessonReviewWriteActivity::class.java)
        intent.putExtra("lessonReview", lessonItem)
        startActivity(intent)
    }

    private fun startVideo(path: String?) {
        path?.let {
            val intent = Intent(this, VideoActivity::class.java)
            intent.putExtra("path", it)
            startActivity(intent)
        }
    }
}
