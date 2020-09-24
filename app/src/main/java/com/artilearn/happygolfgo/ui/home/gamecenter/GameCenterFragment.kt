package com.artilearn.happygolfgo.ui.home.gamecenter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.databinding.FragmentGamecenterBinding

class GameCenterFragment : Fragment() {

    lateinit var binding: FragmentGamecenterBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_gamecenter, container, false)
        return binding.root
    }
}