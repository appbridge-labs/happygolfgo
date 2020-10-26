package com.artilearn.happygolfgo.ui.comment

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.RatingBar
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.base.BaseActivity
import com.artilearn.happygolfgo.constants.COACH_NAME
import com.artilearn.happygolfgo.constants.LESSON_DATE
import com.artilearn.happygolfgo.constants.LESSON_ID
import com.artilearn.happygolfgo.databinding.ActivityCommetBinding
import com.artilearn.happygolfgo.enummodel.BottimViewType
import com.artilearn.happygolfgo.enummodel.ToastType
import com.artilearn.happygolfgo.ui.home.HomeActivity
import com.artilearn.happygolfgo.ui.mypage.MyPageActivity
import com.artilearn.happygolfgo.util.MyApplication
import com.artilearn.happygolfgo.util.convertTime
import com.artilearn.happygolfgo.util.snackbar
import io.reactivex.BackpressureStrategy
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.custom_bottom_view.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.concurrent.TimeUnit

class CommentActivity : BaseActivity<ActivityCommetBinding>(R.layout.activity_commet) {

    private lateinit var disposable: Disposable
    //private lateinit var myApplication: MyApplication

    private val lessonId by lazy {
        intent.getIntExtra(LESSON_ID, 0)
    }

    private val coachName by lazy {
        intent.getStringExtra(COACH_NAME)
    }

    private val lessonDate by lazy {
        intent.getStringExtra(LESSON_DATE)
    }

    private val viewModel: CommentViewModel by viewModel()

//    private val viewModel: CommentViewModel by viewModels {
//        object : ViewModelProvider.Factory {
//            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
//                return CommentViewModel(
//                    myApplication.commentRepository
//                ) as T
//            }
//        }
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //inject()
        initBinding()
        initViewModelObserve()
        initView()
    }

    private fun initBinding() {
        binding.vm = viewModel
        viewModel.lessonId = lessonId
    }

//    private fun inject() {
//        myApplication = application as MyApplication
//        binding.vm = viewModel
//        viewModel.lessonId = lessonId
//    }

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
        initToolbar(binding.commentToolbar.toolbar, getString(R.string.log_toolbar_title), true)
        initBottomView(binding.commentBottomView.bottom_layout, getString(R.string.bottom_view_default), BottimViewType.DEFAULT_TYPE)
        setCoachNameLessonTime(coachName, lessonDate)
    }

    private fun showEmptyRating() {
        binding.commentLayout.snackbar(getString(R.string.comment_empty_rating))
    }

    private fun showEmptyComment() {
        binding.commentLayout.snackbar(getString(R.string.comment_empty_comment))
    }

    private fun goMyPage() {
        showToast(ToastType.SUCCESS, getString(R.string.comment_upload_success))
        val myPageIntent = Intent(this, HomeActivity::class.java)
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
            .subscribe({ comment -> }, {  })
    }

    private fun setCoachNameLessonTime(name: String, date: String) {
        val time = convertTime(date).trim().split(":")
        binding.tvCoachInfo.text =
            "$name, ${time[0]}.${time[1]}.${time[2]}"
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