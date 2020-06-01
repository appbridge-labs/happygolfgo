package com.artilearn.happygolfgo.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.artilearn.happygolfgo.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tlsolution.tlsmodules.Network.RequestEnvironmentMode

class DeveloperFragment : BottomSheetDialogFragment() {
    private lateinit var production: TextView
    private lateinit var test: TextView
    private lateinit var developer: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.developer_server, container, false)

        production = view.findViewById(R.id.tv_production)
        test = view.findViewById(R.id.tv_test)
        developer = view.findViewById(R.id.tv_developer)

        production.setOnClickListener {
            RequestEnvironmentMode.setCurrentMode(RequestEnvironmentMode.production, activity!!)
            Toast.makeText(activity, "production", Toast.LENGTH_SHORT).show()
            dismiss()
        }
        test.setOnClickListener {
            RequestEnvironmentMode.setCurrentMode(RequestEnvironmentMode.test, activity!!)
            Toast.makeText(activity, "test", Toast.LENGTH_SHORT).show()
            dismiss()
        }
        developer.setOnClickListener {
            RequestEnvironmentMode.setCurrentMode(RequestEnvironmentMode.development, activity!!)
            Toast.makeText(activity, "developer", Toast.LENGTH_SHORT).show()
            dismiss()
        }

        return view
    }

}