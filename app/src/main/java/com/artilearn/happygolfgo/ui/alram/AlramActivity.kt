package com.artilearn.happygolfgo.ui.alram

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.base.BaseActivity
import com.artilearn.happygolfgo.databinding.ActivityAlramBinding
import com.artilearn.happygolfgo.util.MyApplication
import kotlinx.android.synthetic.main.activity_alram.*
import kotlinx.android.synthetic.main.custom_toolbar.view.*

class AlramActivity : BaseActivity<ActivityAlramBinding>(R.layout.activity_alram) {
    private val TAG = javaClass.simpleName

    private lateinit var myApplication: MyApplication
    private val viewModel: AlramViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return AlramViewModel(

                ) as T
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    private fun inject() {

    }

    private fun initViewModelObserve() {

    }

    private fun initView() {
        initToolbar(alram_toolbar.toolBar, "dddd", true)
    }
}