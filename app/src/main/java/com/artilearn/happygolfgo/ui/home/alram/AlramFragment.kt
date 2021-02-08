package com.artilearn.happygolfgo.ui.home.alram

import android.content.Intent
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.api.firebase.FCMMessage.Companion.reservationLesson
import com.artilearn.happygolfgo.api.firebase.FCMMessage.Companion.reservationPlate
import com.artilearn.happygolfgo.base.BaseFragment
import com.artilearn.happygolfgo.base.BaseSwipeDelete
import com.artilearn.happygolfgo.constants.LESSON_ID
import com.artilearn.happygolfgo.constants.PLATE_ID
import com.artilearn.happygolfgo.constants.RESERVATION_TYPE
import com.artilearn.happygolfgo.databinding.FragmentAlramBinding
import com.artilearn.happygolfgo.enummodel.ToastType
import com.artilearn.happygolfgo.ui.log.LogActivity
import com.artilearn.happygolfgo.ui.reserveconfirm.ReserveConfirmActivity
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator
import org.koin.androidx.viewmodel.ext.android.viewModel

class AlramFragment
    : BaseFragment<FragmentAlramBinding, AlramViewModel>(R.layout.fragment_alram) {

    override val viewModel: AlramViewModel by viewModel()
    private lateinit var adapter: AlramAdapter

    override fun init() {

        initBinding()
        initViewModelObserving()
        initAdapter()
    }

    private fun initBinding() {
        binding.vm = viewModel
    }

    private fun initViewModelObserving() {
        with(viewModel) {
            serverError.observe(this@AlramFragment, Observer {
                showFragmentToast(ToastType.ERROR, it)
            })
            networkFail.observe(this@AlramFragment, Observer {
                showFragmentToast(ToastType.WARNNING, getString(R.string.fragment_network_fail_content))
            })
            appRefresh.observe(this@AlramFragment, Observer {
                goRefreshFromFragment()
            })
        }
    }

    private fun initAdapter() {
        adapter = AlramAdapter { alram ->
            when (alram.eventType) {
                "3", "7" -> goReservaitonPlate(alram.eventValue.toInt())
                "4" -> goReservationLesson(alram.eventValue.toInt())
                "6" -> goLessonLog(alram.eventValue.toInt())
            }
        }

        with(binding) {
            recyclerview.itemAnimator = SlideInUpAnimator().apply {
                addDuration = 300
            }
            //swipeToDelete(recyclerview)
            recyclerview.adapter = adapter
        }
    }

    private fun swipeToDelete(rv: RecyclerView) {
        val swipeCallback = object : BaseSwipeDelete() {
            override fun onSwipe(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                // 삭제 API 추가되면 활성화
//                val currentItem = adapter.currentList[viewHolder.adapterPosition]
//                viewModel.deleteToAlram()
                val testList = adapter.currentList.toMutableList()
                val deleteItem = testList[viewHolder.adapterPosition]
                testList.remove(deleteItem)
                adapter.submitList(testList)
            }
        }

        val itemHelper = ItemTouchHelper(swipeCallback)
        itemHelper.attachToRecyclerView(rv)
    }

    private fun goReservationLesson(lessonId: Int) {
        val lessonIntent = Intent(activity, ReserveConfirmActivity::class.java)
            .apply {
                putExtra(RESERVATION_TYPE, reservationLesson)
                putExtra(LESSON_ID, lessonId)
            }
        startActivity(lessonIntent)
    }

    private fun goReservaitonPlate(plateId: Int) {
        val plateIntent = Intent(activity, ReserveConfirmActivity::class.java)
            .apply {
                putExtra(RESERVATION_TYPE, reservationPlate)
                putExtra(PLATE_ID, plateId)
            }
        startActivity(plateIntent)
    }

    private fun goLessonLog(lessonId: Int) {
        val logIntent = Intent(activity, LogActivity::class.java)
            .apply {
                putExtra(LESSON_ID, lessonId)
            }
        startActivity(logIntent)
    }

}