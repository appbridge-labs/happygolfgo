package com.artilearn.happygolfgo.ui.alram

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.base.BaseActivity
import com.artilearn.happygolfgo.constants.LESSON_ID
import com.artilearn.happygolfgo.constants.PLATE_ID
import com.artilearn.happygolfgo.constants.RESERVATION_TYPE
import com.artilearn.happygolfgo.databinding.ActivityAlramBinding
import com.artilearn.happygolfgo.enummodel.ToastType
import com.artilearn.happygolfgo.ui.log.LogActivity
import com.artilearn.happygolfgo.ui.reserveconfirm.ReserveConfirmActivity
import com.artilearn.happygolfgo.util.MyApplication

class AlramActivity : BaseActivity<ActivityAlramBinding>(R.layout.activity_alram) {

    private lateinit var adapter: AlramAdapter
    private lateinit var myApplication: MyApplication
    private val reservationPlate = "ReservationPlateFromAlram"
    private val reservationLesson = "ReservationLessonFromAlram"

    private val viewModel: AlramViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return AlramViewModel(
                    myApplication.alramRepository
                ) as T
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        inject()
        initViewModelObserve()
        initView()
        initAdapter()
    }

    private fun inject() {
        myApplication = application as MyApplication
        binding.vm = viewModel
    }

    private fun initViewModelObserve() {
        with(viewModel) {
            serverError.observe(this@AlramActivity, Observer {
                showToast(ToastType.ERROR, it)
            })
            networkFail.observe(this@AlramActivity, Observer {
                showToast(ToastType.WARNNING, getString(R.string.fragment_network_fail_content))
            })
        }
    }

    private fun initView() {
        initToolbar(binding.alramToolbar.toolbar, getString(R.string.alram_toolbar_title), true)
    }

    private fun initAdapter() {
        adapter = AlramAdapter { alram ->
            when (alram.eventType) {
                "3" -> {
                    goReservaitonPlate(alram.eventValue.toInt())
                }
                "4" -> {
                    goReservationLesson(alram.eventValue.toInt())
                }
                "6" -> goLessonLog(alram.eventValue.toInt())
            }
        }

        binding.recyclerview.adapter = adapter
    }

    private fun goReservationLesson(lessonId: Int) {
        val lessonIntent = Intent(this, ReserveConfirmActivity::class.java)
            .apply {
                putExtra(RESERVATION_TYPE, reservationLesson)
                putExtra(LESSON_ID, lessonId)
            }
        startActivity(lessonIntent)
    }

    private fun goReservaitonPlate(plateId: Int) {
        val plateIntent = Intent(this, ReserveConfirmActivity::class.java)
            .apply {
                putExtra(RESERVATION_TYPE, reservationPlate)
                putExtra(PLATE_ID, plateId)
            }
        startActivity(plateIntent)
    }

    private fun goLessonLog(lessonId: Int) {
        val logIntent = Intent(this, LogActivity::class.java)
            .apply {
                putExtra(LESSON_ID, lessonId)
            }
        startActivity(logIntent)
    }

}