package com.artilearn.happygolfgo.ui.user

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.adapter.LessonReviewListAdapter
import com.artilearn.happygolfgo.library.HappyGolfGoRequest
import com.artilearn.happygolfgo.library.HappyGolfGoUserManager
import com.artilearn.happygolfgo.models.LessonReviewList
import com.artilearn.happygolfgo.ui.BaseActivity
import com.google.gson.internal.LinkedTreeMap
import com.tlsolution.tlsmodules.Network.RequestManager
import com.tlsolution.tlsmodules.Network.RequestResult
import kotlinx.android.synthetic.main.activity_lesson_list.*
import kotlinx.android.synthetic.main.activity_lesson_list.progressBar
import kotlinx.android.synthetic.main.activity_sign_in.*
import kotlinx.android.synthetic.main.partial_toolbar.view.*

class LessonListActivity : BaseActivity() {
    private val TAG = javaClass.simpleName

    private lateinit var requestManager: RequestManager
    private lateinit var userManager: HappyGolfGoUserManager
    private lateinit var adapter: LessonReviewListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lesson_list)

        initView()
        requestLessonReviewList()
        initAdapter()
    }

    private fun initView() {
        requestManager = RequestManager(this)
        userManager = HappyGolfGoUserManager(this)

        setUpActionBar(toolbar_lessonReview.toolbar, "레슨 일지", true)
    }

    private fun initAdapter() {
        adapter = LessonReviewListAdapter { item ->
            goLessonReview(item)
        }

        lr_recyclerView.adapter = adapter
    }

    private fun isLoading(loading: Boolean) {
        progressBar.visibility = if (loading) {
            View.VISIBLE
        } else {
            View.INVISIBLE
        }
    }

    private fun requestLessonReviewList() {
        isLoading(true)
        val request = HappyGolfGoRequest.lessonReviewList
        requestManager.request(request, userManager.user) { result, json ->
            when (result) {
                RequestResult.nSuccess -> {
                    runOnUiThread { isLoading(false) }
                }
                RequestResult.pSuccess -> {
                    runOnUiThread { isLoading(false) }
                    when (result.statusCode) {
                        200 -> {
                            val lessonJson = json?.get("lessonLogs") as ArrayList<LinkedTreeMap<String, Any>>
                            val item = lessonJson.mapNotNull { LessonReviewList.create(it) } as ArrayList<LessonReviewList>

                            if (item.size > 0) {
                                runOnUiThread {
                                    adapter.setItems(item)
                                }
                            } else {
                                runOnUiThread {
                                    emptyLessonLayout.visibility = View.VISIBLE
                                }
                            }
                        }
                    }
                }
                RequestResult.failure -> {
                    runOnUiThread { isLoading(false) }
                }
                else -> {
                    runOnUiThread { isLoading(false) }
                }
            }
        }
    }

    private fun goLessonReview(lessonReview: LessonReviewList) {
        val intent = Intent(this, LessonReviewActivity::class.java)
        intent.putExtra("lessonReview", lessonReview)
        startActivity(intent)
    }
}
