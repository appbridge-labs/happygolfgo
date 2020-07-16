package com.artilearn.happygolfgo.ui.alram

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.base.BaseActivity
import com.artilearn.happygolfgo.databinding.ActivityAlramBinding
import com.artilearn.happygolfgo.enummodel.ToastType
import com.artilearn.happygolfgo.util.MyApplication
import kotlinx.android.synthetic.main.activity_alram.*
import kotlinx.android.synthetic.main.custom_toolbar.view.*

class AlramActivity : BaseActivity<ActivityAlramBinding>(R.layout.activity_alram) {
    private val TAG = javaClass.simpleName

    private lateinit var adapter: AlramAdapter
    private lateinit var myApplication: MyApplication
    private val viewModel: AlramViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return AlramViewModel(
                    myApplication.alramRepository
                ) as T
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        inject()
        initViewModelObserve()
        initView()
        initAdapter()
    }

    private fun inject() {
        myApplication = application as MyApplication
        binding.vm = viewModel
    }

    private fun initViewModelObserve() {
        with(viewModel) {

        }
    }

    private fun initView() {
        initToolbar(alram_toolbar.toolBar, getString(R.string.alram_toolbar_title), true)
    }

    private fun initAdapter() {
        adapter = AlramAdapter { alram ->
            showToast(ToastType.SUCCESS, "$alram")
        }

        binding.recyclerView.adapter = adapter
    }
}