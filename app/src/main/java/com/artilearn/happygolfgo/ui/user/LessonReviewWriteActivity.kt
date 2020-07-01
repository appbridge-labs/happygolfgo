package com.artilearn.happygolfgo.ui.user

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.library.HappyGolfGoRequest
import com.artilearn.happygolfgo.library.HappyGolfGoUserManager
import com.artilearn.happygolfgo.models.LessonReviewDetail
import com.artilearn.happygolfgo.models.LessonReviewList
import com.artilearn.happygolfgo.ui.BaseActivity
import com.artilearn.happygolfgo.util.convertTime
import com.artilearn.happygolfgo.util.noticeDialog
import com.google.gson.internal.LinkedTreeMap
import com.tlsolution.tlsmodules.Network.RequestManager
import com.tlsolution.tlsmodules.Network.RequestResult
import kotlinx.android.synthetic.main.activity_lesson_review_write.*
import kotlinx.android.synthetic.main.activity_lesson_review_write.progressBar
import kotlinx.android.synthetic.main.activity_sign_in.*
import kotlinx.android.synthetic.main.partial_bottom_button.view.*
import kotlinx.android.synthetic.main.partial_toolbar.view.*

class LessonReviewWriteActivity : BaseActivity() {
private val TAG = javaClass.simpleName
    private lateinit var requestManager: RequestManager
    private lateinit var userManager: HappyGolfGoUserManager
    private lateinit var request: HappyGolfGoRequest
    private var lessonReviewList: LessonReviewList? = null
    private var comment: String? = null
    private var grade: Float = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lesson_review_write)

        initView()
    }

    private fun initView() {
        requestManager = RequestManager(this)
        userManager = HappyGolfGoUserManager(this)

        lessonReviewList = intent.getSerializableExtra("lessonReview") as LessonReviewList

        setUpActionBar(write_toolbar.toolbar, "레슨 후기", true)
        bottom_nav.text_view_title_bottom_button.text = "확인"
        tv_length.text = "0/100"
        bottom_nav.setOnClickListener { uploadLessonReviewBefore() }

        mappingData()
        commentLength()
    }

    private fun mappingData() {
        val date = lessonReviewList?.createdAt
        val name = lessonReviewList?.employeeName

        if (!date.isNullOrEmpty() && !name.isNullOrEmpty()) {
            val time = convertTime(date).trim().split(":")
            tv_coach_name_date.text = "$name, ${time[0]}.${time[1]}.${time[2]}"
        }
    }

    private fun commentLength() {
        et_comment.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s.toString().length > 100) {
                    noticeDialog(this@LessonReviewWriteActivity, "글자수 제한", "100자 이내로 작성해 주세요")
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val length = s?.toString()
                tv_length.text = "${length?.length}/100"
            }
        })
    }

    private fun uploadLessonReviewBefore() {
        if (et_comment.text.isNotEmpty() && ratingBar.rating != 0f) {
            Log.d(TAG, "zzzzzz")
            comment = et_comment.text.toString()
            grade = ratingBar.rating
            uploadLessonReview()
        } else {
            when {
                et_comment.text.isEmpty() -> {
                    noticeDialog(this, "레슨 후기", "후기를 작성해 주세요")
                }
                ratingBar.rating == 0f -> {
                    noticeDialog(this, "레슨 후기", "평점을 등록해 주세요")
                }
                else -> {
                    noticeDialog(this, "레슨 후기", "형식이 올바르지 않습니다")
                }
            }
        }
    }

    private fun isLoading(loading: Boolean) {
        progressBar.visibility = if (loading) {
            View.VISIBLE
        } else {
            View.INVISIBLE
        }
    }

    private fun uploadLessonReview() {
        isLoading(true)

        request = HappyGolfGoRequest.uploadLessonReview
        request.body =
            "{\"Data\": {\"lessonLogID\": \"${lessonReviewList?.id}\", \"comment\": \"$comment\", \"ratings\": \"$grade\" }}"

        requestManager.request(request, userManager.user) { result, json ->
            when (result) {
                RequestResult.nSuccess -> {
                    Log.d(TAG, "nnnnnnnn!!!!: ")
                    runOnUiThread { isLoading(false) }
                }
                RequestResult.pSuccess -> {
                    when (result.statusCode) {
                        200 -> {
                            Log.d(TAG, "200!!!!: ")
                            runOnUiThread {
                                isLoading(false)
                            }
                        }
                        406 -> {
                            Log.d(TAG, "406!!!!: ")
                            runOnUiThread {
                                isLoading(false)
                            }
                        }
                    }
                }
                RequestResult.failure -> {
                    Log.d(TAG, "failure!!!!: ")
                    runOnUiThread { isLoading(false) }
                }
                else -> {
                    Log.d(TAG, "else!!!!: ")
                    runOnUiThread { isLoading(false) }
                }
            }
        }
    }

}
