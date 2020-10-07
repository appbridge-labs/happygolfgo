package com.artilearn.happygolfgo.ui.home

import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.base.BaseActivity
import com.artilearn.happygolfgo.databinding.ActivityHomeBinding
import com.artilearn.happygolfgo.ui.home.alram.AlramFragment
import com.artilearn.happygolfgo.ui.home.gamecenter.GameCenterFragment
import com.artilearn.happygolfgo.ui.home.mypage.MyPageFragment
import com.artilearn.happygolfgo.ui.home.reservation.ReservationFragment
import com.artilearn.happygolfgo.util.MyApplication
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : BaseActivity<ActivityHomeBinding>(R.layout.activity_home) {

    //    private lateinit var adapter: HomeAdapter
    //    private var possible = false
//
//    private val fcmEventType by lazy {
//        intent.getStringExtra(FCM_EVENT_TYPE)
//    }
//
//    private val reservationType by lazy {
//        intent.getStringExtra(RESERVATION_TYPE)
//    }
//
//    private val lessonId by lazy {
//        intent.getIntExtra(LESSON_ID, 0)
//    }
//
//    private val plateId by lazy {
//        intent.getIntExtra(PLATE_ID, 0)
//    }
//
    private lateinit var myApplication: MyApplication
    private val vm: HomeViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return HomeViewModel() as T
            }
        }
    }

//    private lateinit var reservation: ReservationFragment
//    private lateinit var gamecenter: GameCenterFragment
//    private lateinit var alram: AlramFragment
//    private lateinit var mypage: MyPageFragment
    private lateinit var bottomNavigation: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initBinding()
        initViewModelObserving()
        initView()
        bottomNavigationListener()
    }

    private fun initBinding() {
        myApplication = application as MyApplication
        binding.vm = vm
    }

    private fun initViewModelObserving() {
        with(vm) {
            bottomNavigationButton.observe(this@HomeActivity, Observer {
                when (bottomNavigationButton.value) {
                    HomeViewModel.BottomNavigationEvent.RESERVATION -> println("reservaiton")
                    HomeViewModel.BottomNavigationEvent.GAME_CENTER -> println("game")
                    HomeViewModel.BottomNavigationEvent.ALRAM -> println("alram")
                    HomeViewModel.BottomNavigationEvent.MY_PAGE -> println("mypage")
                }
            })
//            bottomReservation.observe(this@HomeActivity, Observer {
//                if (it) { showReservationFragment(it) }
//            })
//            bottomGameCenter.observe(this@HomeActivity, Observer {
//                if (it) { showGameCenterFragment(it) }
//            })
//            bottomAlram.observe(this@HomeActivity, Observer {
//                if (it) { showAlramFragment(it) }
//            })
//            bottomMyPage.observe(this@HomeActivity, Observer {
//                if (it) { showMyPageFragment(it) }
//            })
        }
    }

    private fun initView() {
        initToolbar(binding.homeToolbar.toolbar, getString(R.string.home_toolbar_text), false)
        bottomNavigation = binding.bottomNavigation

        //reservation = ReservationFragment()
        //loadFragment(reservation)
    }

    private fun bottomNavigationListener() {
        bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.reservation -> { vm.bottomNavigation(RESERVATION) }
                R.id.gamecenter -> { vm.bottomNavigation(GAME_CENTER) }
                R.id.alram -> { vm.bottomNavigation(ALRAM) }
                R.id.mypage -> { vm.bottomNavigation(MY_PAGE) }
            }

            true
        }
    }

    private fun showReservationFragment(visibile: Boolean) {
//        if (visibile) {
//            initToolbar(binding.homeToolbar.toolbar, getString(R.string.home_toolbar_text), false)
//            reservation = ReservationFragment()
//            loadFragment(reservation)
//        }
    }

    private fun showGameCenterFragment(visibile: Boolean) {
//        if (visibile) {
//            initToolbar(binding.homeToolbar.toolbar, getString(R.string.gamecenter_toolbar_title), false)
//            gamecenter = GameCenterFragment()
//            loadFragment(gamecenter)
//        }
    }

    private fun showAlramFragment(visibile: Boolean) {
//        if (visibile) {
//            initToolbar(binding.homeToolbar.toolbar, getString(R.string.alram_toolbar_title), false)
//            alram = AlramFragment()
//            loadFragment(alram)
//        }
    }

    private fun showMyPageFragment(visibile: Boolean) {
//        if (visibile) {
//            initToolbar(binding.homeToolbar.toolbar, getString(R.string.mypage_toolbar_title), false)
//            mypage = MyPageFragment()
//            loadFragment(mypage)
//        }
    }

    private fun loadFragment(fragment: Fragment) {
//        val manager = supportFragmentManager.beginTransaction()
//        manager.replace(R.id.frame_layout, fragment)
//        manager.addToBackStack(null)
//        manager.commit()
    }


    companion object {
        const val RESERVATION = "예약"
        const val GAME_CENTER = "림게임센터"
        const val ALRAM = "알"
        const val MY_PAGE = "마이페이지"
    }






//    private fun inject() {
//        myApplication = application as MyApplication
//        binding.vm = viewModel
//    }
//
//    private fun initViewModelObserve() {
//        with(viewModel) {
//            myPage.observe(this@HomeActivity, Observer {
//                //goMyPage()
//            })
//            alram.observe(this@HomeActivity, Observer {
//                //goAlram()
//            })
//            serverError.observe(this@HomeActivity, Observer {
//                showError(it)
//            })
//            networkFail.observe(this@HomeActivity, Observer {
//                showNetworkFail()
//            })
//        }
//    }

//    private fun initView() {
//        initToolbar(binding.homeToolbar.toolbar, getString(R.string.home_toolbar_text), false)
//        initBottomView(
//            binding.includeBottomView,
//            getString(R.string.home_bottomview_title),
//            BottimViewType.DEFAULT_TYPE
//        )
//
//        when (fcmEventType) {
//            "3" -> goReservaitonPlateFromFCM()
//            "4" -> goReservationLessonFromFCM()
//            "6" -> goLessonLogFromFCM()
//            "" -> goAlram()
//        }
//    }

//    private fun initAdapter() {
//        adapter = HomeAdapter { ticket ->
//            with(ticket) {
//                when {
//                    !isUnlimited && remainingCount == 0 -> binding.parentLayout.snackbar(getString(R.string.home_fail_ticket))
//                    allowToBook == possible -> binding.parentLayout.snackbar(getString(R.string.home_reservation_allowbook_fail))
//                    ticket.type == 1 -> ""
//                    else -> goSelectionDate(ticket)
//                }
//            }
//        }
//
//        binding.recyclerview.adapter = adapter
//    }

//    private fun ticketRefresh() {
//        with(binding.swipeRefresh) {
//            setOnRefreshListener {
//                viewModel.requestTicket()
//                isRefreshing = false
//            }
//
//            setColorSchemeColors(resources.getColor(R.color.color_dark_green))
//        }
//    }

//    private fun goMyPage() {
//        startActivity(Intent(this, MyPageActivity::class.java))
//    }
//
//    private fun goAlram() {
//        startActivity(Intent(this, AlramActivity::class.java))
//    }

//    private fun goSelectionDate(ticket: Ticket) {
//        val selectIntent = Intent(this, TicketDateActivity::class.java)
//            .apply {
//                putExtra(TICKET, ticket)
//            }
//        startActivity(selectIntent)
//    }
//
//    private fun goReservaitonPlateFromFCM() {
//        val fcmIntent = Intent(this, ReserveConfirmActivity::class.java)
//            .apply {
//                putExtra(RESERVATION_TYPE, reservationType)
//                putExtra(PLATE_ID, plateId)
//            }
//        startActivity(fcmIntent)
//    }
//
//    private fun goReservationLessonFromFCM() {
//        val fcmIntent = Intent(this, ReserveConfirmActivity::class.java)
//            .apply {
//                putExtra(RESERVATION_TYPE, reservationType)
//                putExtra(LESSON_ID, lessonId)
//            }
//        startActivity(fcmIntent)
//    }
//
//    private fun goLessonLogFromFCM() {
//        val logIntent = Intent(this, LogActivity::class.java)
//            .apply {
//                putExtra(LESSON_ID, lessonId)
//            }
//        startActivity(logIntent)
//    }
//
//    override fun bottomAction() {
//        super.bottomAction()
//
//        if (adapter.itemCount != 0) {
//            val reserveListActivity = Intent(this, ReserveListActivity::class.java)
//            startActivity(reserveListActivity)
//        } else {
//            binding.parentLayout.snackbar(getString(R.string.home_empty_ticket_go))
//        }
//    }


}