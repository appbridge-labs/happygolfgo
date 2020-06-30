package com.artilearn.happygolfgo.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.adapter.LessonReservationAdapter
import com.artilearn.happygolfgo.models.LessonReservation
import com.artilearn.happygolfgo.ui.user.ConfirmReservationActivity
import com.google.gson.internal.LinkedTreeMap
import kotlinx.android.synthetic.main.fragment_lesson_reservation_list.*


class LessonReservationListFragment(private val fragmentData: LinkedTreeMap<String, Any>) :
    Fragment(), LessonReservationAdapter.LessonClick {

    private lateinit var lessonReservationAdapter: LessonReservationAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_lesson_reservation_list, container, false)

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setUpAdapter()
        fetchData()
    }

    override fun lesson(lesson: LessonReservation) {
        val intent = Intent(activity, ConfirmReservationActivity::class.java)
        intent.putExtra("item", lesson)
        activity!!.startActivity(intent)
    }

    private fun setUpAdapter() {
        lessonReservationAdapter =
            LessonReservationAdapter()
        recycler_view_lesson_reservation_list.adapter = lessonReservationAdapter
        lessonReservationAdapter.listener = this
    }

    private fun itemCheck(v: Boolean) {
        constrainLayout.visibility = if (v) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    private fun fetchData() {
        val lessonJson =
            fragmentData["lessonReservations"] as? ArrayList<LinkedTreeMap<String, Any>>

        val item =
            lessonJson?.mapNotNull { LessonReservation.create(it) } as? ArrayList<LessonReservation>


        if (item != null) {
            itemCheck(false)
            lessonReservationAdapter.setItem(item)
        }

        if (lessonReservationAdapter.itemCount == 0) itemCheck(true)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        lessonReservationAdapter.itemClear()
    }
}
