package com.artilearn.happygolfgo.ui.comment

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
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
import com.artilearn.happygolfgo.ui.mypage.MyPageActivity
import com.artilearn.happygolfgo.util.MyApplication
import com.artilearn.happygolfgo.util.snackbar
import io.reactivex.BackpressureStrategy
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.custom_bottom_view.view.*
import java.util.concurrent.TimeUnit

class CommentActivity : BaseActivity<ActivityCommetBinding>(R.layout.activity_commet) {
    private val TAG = javaClass.simpleName

    private lateinit var disposable: Disposable
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
                showToast(ToastType.ERROR, getString(R.string.comment_upload_fail) + "\n$errorMsg")
            })
            success.observe(this@CommentActivity, Observer {
                goMyPage()
            })
        }
    }

    private fun initView() {
        initToolbar(binding.commentToolbar.toolBar, getString(R.string.log_toolbar_title), true)
        initBottomView(
            binding.commentBottomView.bottomLayout,
            getString(R.string.bottom_view_default),
            BottimViewType.DEFAULT_TYPE
        )
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

    private fun createCommentObservable(): Observable<String> {
        val comment = Observable.create<String> { emitter ->
            val textWatcher = object : TextWatcher {
                override fun afterTextChanged(s: Editable?) = Unit

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    s?.toString()?.let {
                        val text = s.toString()
                        binding.tvLength.text = "${text.length}/100"
                        emitter.onNext(it)
                    }
                }
            }

            with(binding) {
                etComment.addTextChangedListener(textWatcher)
                emitter.setCancellable {
                    etComment.removeTextChangedListener(textWatcher)
                }
            }
        }

        return comment
            .debounce(500, TimeUnit.MILLISECONDS)
    }

    private fun rxComment() {
        val flowable = createCommentObservable()
            .toFlowable(BackpressureStrategy.BUFFER)

        disposable = flowable
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ comment ->
                if (comment.length > 100) {
                    binding.etComment.error = getString(R.string.comment_fail_length)
                }
            }, { t ->
                Log.d(TAG, "rxComment: ${t.message}")
            })
    }

    override fun onStart() {
        super.onStart()
        rxComment()
    }

    override fun bottomAction() {
        super.bottomAction()
        viewModel.uploadLessonCommet()
    }

}