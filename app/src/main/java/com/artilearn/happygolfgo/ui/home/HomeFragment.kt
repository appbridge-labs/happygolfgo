package com.artilearn.happygolfgo.ui.home

import android.content.Intent
import android.widget.Toast
import androidx.core.content.ContextCompat.getColor
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.base.BaseFragment
import com.artilearn.happygolfgo.databinding.FragmentHomeBinding
import com.artilearn.happygolfgo.ui.calendar.CalendarActivity
import com.artilearn.happygolfgo.ui.common.NetworkFailFragment
import com.artilearn.happygolfgo.ui.common.ServerErrorFragment
import com.artilearn.happygolfgo.util.MyApplication

class HomeFragment
    : BaseFragment<FragmentHomeBinding, HomeFragmentViewModel>(R.layout.fragment_home) {
    
    companion object {

        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }

    private lateinit var adapter: HomeAdapter
    private lateinit var myApplication: MyApplication
    override val viewModel: HomeFragmentViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return HomeFragmentViewModel(
                    myApplication.homeRepository
                ) as T
            }
        }
    }

    override fun init() {
        myApplication = activity?.application as MyApplication
        binding.vm = viewModel

        initViewModelObserve()
        initAdapter()
        ticketRefresh()
    }

    private fun initViewModelObserve() {
        with(viewModel) {
            networkFail.observe(this@HomeFragment, Observer {
                showNetworkFail()
            })
            serverError.observe(this@HomeFragment, Observer {
                showServerError(it)
            })
        }
    }

    private fun initAdapter() {
        adapter = HomeAdapter { ticket ->
            Toast.makeText(activity, "$ticket", Toast.LENGTH_SHORT).show()
            goCalendar()
        }

        binding.recyclerView.adapter = adapter
    }

    private fun ticketRefresh() {
        with(binding.swipeRefresh) {
            setOnRefreshListener {
                viewModel.requestTicket()
                isRefreshing = false
            }

            setColorSchemeColors(getColor(context, R.color.color_dark_green))
        }
    }

    private fun showServerError(msg: String) {
        val serverError = ServerErrorFragment.newInstance(msg)
        activity?.supportFragmentManager!!.beginTransaction()
            .replace(R.id.homeFrameLayout, serverError)
            .commit()
    }

    private fun showNetworkFail() {
        val networkFail = NetworkFailFragment.newInstance()
        activity?.supportFragmentManager!!.beginTransaction()
            .replace(R.id.homeFrameLayout, networkFail)
            .commit()
    }

    private fun goCalendar() {
        startActivity(Intent(activity, CalendarActivity::class.java))
    }

}