package com.artilearn.happygolfgo.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.ui.BaseActivity
import kotlinx.android.synthetic.main.fragment_network_failure.*

class NetworkFailureFragment(private val rootActivity: BaseActivity) : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_network_failure, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpClickListener()
    }

    private fun setUpClickListener() {
        button_retry_network_failure.setOnClickListener {
            rootActivity.retryAction()
        }
    }

}
