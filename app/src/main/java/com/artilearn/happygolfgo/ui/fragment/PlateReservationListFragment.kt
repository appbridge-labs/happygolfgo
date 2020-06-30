package com.artilearn.happygolfgo.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.adapter.PlateReservationAdapter
import com.artilearn.happygolfgo.models.PlateReservation
import com.artilearn.happygolfgo.ui.user.ConfirmReservationActivity
import com.google.gson.internal.LinkedTreeMap
import kotlinx.android.synthetic.main.fragment_plate_reservation_list.*

class PlateReservationListFragment(private val fragmentData: LinkedTreeMap<String, Any>) :
    Fragment(), PlateReservationAdapter.ReserveClick {

    private lateinit var plateReservationAdapter: PlateReservationAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_plate_reservation_list, container, false)

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setUpAdapter()
        fetchData()
    }

    override fun reserve(reserve: PlateReservation) {
        val intent = Intent(activity, ConfirmReservationActivity::class.java)
        intent.putExtra("item", reserve)
        activity!!.startActivity(intent)
    }

    private fun setUpAdapter() {
        plateReservationAdapter =
            PlateReservationAdapter()
        recycler_view_plate_reservation_list.adapter = plateReservationAdapter
        plateReservationAdapter.listener = this
    }

    private fun itemCheck(v: Boolean) {
        constrainLayout.visibility = if (v) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    private fun fetchData() {
        val plateReservationsJSON =
            fragmentData["plateReservations"] as? ArrayList<LinkedTreeMap<String, Any>>
        val item =
            plateReservationsJSON?.mapNotNull { PlateReservation.create(it) } as? ArrayList<PlateReservation>


        if (item != null) {
            itemCheck(false)
            plateReservationAdapter.setItem(item)
        }

        if (plateReservationAdapter.itemCount == 0) itemCheck(true)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        plateReservationAdapter.itemClear()
    }
}
