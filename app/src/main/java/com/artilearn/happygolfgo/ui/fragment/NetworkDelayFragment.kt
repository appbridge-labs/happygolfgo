package com.artilearn.happygolfgo.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.ui.BaseActivity
import kotlinx.android.synthetic.main.fragment_network_delay.*

class NetworkDelayFragment(var rootActivity: BaseActivity) : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_network_delay, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setUpClickListener()

    }

    private fun setUpClickListener() {
        button_retry_network_delay.setOnClickListener {
            rootActivity.hideNetworkDelay()
            rootActivity.showLoading()
        }
    }

}
