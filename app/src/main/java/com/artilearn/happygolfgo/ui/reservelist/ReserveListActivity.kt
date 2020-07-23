package com.artilearn.happygolfgo.ui.reservelist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.base.BaseActivity
import com.artilearn.happygolfgo.databinding.ActivityReserveListBinding
import com.artilearn.happygolfgo.util.MyApplication
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_reserve_list.*
import kotlinx.android.synthetic.main.custom_toolbar.view.*

class ReserveListActivity : BaseActivity<ActivityReserveListBinding>(R.layout.activity_reserve_list) {

    private val fragmentList: ArrayList<Fragment> = arrayListOf()
    //private lateinit var adapter: ReserveListAdapter
    private lateinit var myApplication: MyApplication
    private val viewModel: ReserveListViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return ReserveListViewModel(
                    myApplication.reserveListRepository
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

    }

    private fun initView() {
        //fragmentList.add(ReservePlateFragment())
        //fragmentList.add(ReserveLessonFragment())
        initToolbar(reserve_list_toolbar.toolBar, getString(R.string.reserve_list_toolbar_title), true)
    }

    private fun initAdapter() {
//        binding.viewPager.adapter = ReserveListAdapter(this, fragmentList)
//        TabLayoutMediator(binding.reserveListTablayout, binding.viewPager,
//            TabLayoutMediator.TabConfigurationStrategy { tab, position ->
//                // styling each tab here
//            }).attach()

    }

}