package com.artilearn.happygolfgo.ui.home.mypage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.Observer
import com.artilearn.happygolfgo.BR
import com.artilearn.happygolfgo.base.BaseBottomSheetFragment
import com.artilearn.happygolfgo.constants.BUNDLE_KEY
import com.artilearn.happygolfgo.constants.REQUEST_KEY
import com.artilearn.happygolfgo.databinding.FragmentBottomMypageBinding
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class MyPageBottomFragment : BaseBottomSheetFragment() {

    private val viewModel: MyPageViewModel by sharedViewModel()
    private var flag = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBottomMypageBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.setVariable(BR.viewModel, viewModel)

        viewModel.bottomState.observe(viewLifecycleOwner, Observer {
            flag = it
            dismiss()
        })

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()

        if (flag) {
            setFragmentResult(REQUEST_KEY, bundleOf(BUNDLE_KEY to flag))
        }
    }

}