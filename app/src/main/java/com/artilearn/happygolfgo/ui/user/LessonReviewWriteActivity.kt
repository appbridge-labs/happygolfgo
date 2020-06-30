package com.artilearn.happygolfgo.ui.user

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.library.HappyGolfGoRequest
import com.artilearn.happygolfgo.library.HappyGolfGoUserManager
import com.artilearn.happygolfgo.models.LessonReviewList
import com.artilearn.happygolfgo.ui.BaseActivity
import com.artilearn.happygolfgo.util.convertTime
import com.artilearn.happygolfgo.util.noticeDialog
import com.tlsolution.tlsmodules.Network.RequestManager
import kotlinx.android.synthetic.main.activity_lesson_review_write.*
import kotlinx.android.synthetic.main.partial_bottom_button.view.*
import kotlinx.android.synthetic.main.partial_toolbar.view.*

class LessonReviewWriteActivity : BaseActivity() {

    private lateinit var requestManager: RequestManager
    private lateinit var userManager: HappyGolfGoUserManager
    private lateinit var request: HappyGolfGoRequest
    private var lessonReviewList: LessonReviewList? = null

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

    private fun uploadLessonReview() {
        Toast.makeText(this, "a", Toast.LENGTH_SHORT).show()
    }

}
