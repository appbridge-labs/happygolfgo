package com.artilearn.happygolfgo.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.artilearn.happygolfgo.base.BaseBottomSheetFragment
import com.artilearn.happygolfgo.databinding.FragmentBottomLoginBinding

class LoginBottomFragment : BaseBottomSheetFragment() {

//    private lateinit var callback: OnBackPressedCallback
//
//    override fun onAttach(context: Context) {
//        super.onAttach(context)
//        println("onAttach")
//        callback = object : OnBackPressedCallback(false) {
//            override fun handleOnBackPressed() {
//                println("nope nope")
//            }
//        }
//
//        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
//    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        println("onCreateView")
       _binding = FragmentBottomLoginBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this

        binding.root.setOnClickListener {
            println("dismiss")
            dismiss()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        println("onViewCreated")
        dialog?.let {
            it.setCanceledOnTouchOutside(false)
            it.setCancelable(true)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        println("destroy")
        _binding = null
        //callback.remove()
    }

}