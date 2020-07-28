package com.artilearn.happygolfgo.ui.home

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.base.BaseActivity
import com.artilearn.happygolfgo.constants.TICKET
import com.artilearn.happygolfgo.data.Ticket
import com.artilearn.happygolfgo.databinding.ActivityHomeBinding
import com.artilearn.happygolfgo.enummodel.BottimViewType
import com.artilearn.happygolfgo.ui.alram.AlramActivity
import com.artilearn.happygolfgo.ui.ticketdate.TicketDateActivity
import com.artilearn.happygolfgo.ui.mypage.MyPageActivity
import com.artilearn.happygolfgo.ui.reservelist.ReserveListActivity
import com.artilearn.happygolfgo.util.MyApplication
import com.artilearn.happygolfgo.util.snackbar

class HomeActivity : BaseActivity<ActivityHomeBinding>(R.layout.activity_home) {
    private val TAG = javaClass.simpleName

    private lateinit var adapter: HomeAdapter
    private lateinit var myApplication: MyApplication
    private val viewModel: HomeViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return HomeViewModel(
                    myApplication.homeRepository
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
        ticketRefresh()
    }

    private fun inject() {
        myApplication = application as MyApplication
        binding.vm = viewModel
    }

    private fun initViewModelObserve() {
        with(viewModel) {
            myPage.observe(this@HomeActivity, Observer {
                goMyPage()
            })
            alram.observe(this@HomeActivity, Observer {
                goAlram()
            })
            serverError.observe(this@HomeActivity, Observer {
                showError(it)
            })
            networkFail.observe(this@HomeActivity, Observer {
                showNetworkFail()
            })
        }
    }

    private fun initView() {
        initBottomView(binding.includeBottomView, getString(R.string.home_bottomview_title), BottimViewType.DEFAULT_TYPE)
    }

    private fun initAdapter() {
        adapter = HomeAdapter { ticket ->
            goSelectionDate(ticket)
        }

        binding.recyclerview.adapter = adapter
    }

    private fun ticketRefresh() {
        with(binding.swipeRefresh) {
            setOnRefreshListener {
                viewModel.requestTicket()
                isRefreshing = false
            }

            setColorSchemeColors(resources.getColor(R.color.color_dark_green))
        }
    }

    private fun goMyPage() {
        startActivity(Intent(this, MyPageActivity::class.java))
    }

    private fun goAlram() {
        startActivity(Intent(this, AlramActivity::class.java))
    }

    private fun goSelectionDate(ticket: Ticket) {
        val intent = Intent(this, TicketDateActivity::class.java)
        intent.putExtra(TICKET, ticket)
        startActivity(intent)
    }

    override fun bottomAction() {
        super.bottomAction()

        if (adapter.itemCount != 0) {
            val reserveListActivity = Intent(this, ReserveListActivity::class.java)
            startActivity(reserveListActivity)
        } else {
            binding.parentLayout.snackbar(getString(R.string.home_empty_ticket_go))
        }
    }


}