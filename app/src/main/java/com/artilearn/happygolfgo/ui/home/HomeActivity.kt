package com.artilearn.happygolfgo.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.base.BaseActivity
import com.artilearn.happygolfgo.constants.FCM_EVENT_TYPE
import com.artilearn.happygolfgo.constants.LESSON_ID
import com.artilearn.happygolfgo.constants.PLATE_ID
import com.artilearn.happygolfgo.constants.RESERVATION_TYPE
import com.artilearn.happygolfgo.databinding.ActivityHomeBinding
import com.artilearn.happygolfgo.ui.home.alram.AlramFragment
import com.artilearn.happygolfgo.ui.home.gamecenter.GameCenterFragment
import com.artilearn.happygolfgo.ui.home.mypage.MyPageFragment
import com.artilearn.happygolfgo.ui.home.reservation.ReservationFragment
import com.artilearn.happygolfgo.ui.log.LogActivity
import com.artilearn.happygolfgo.ui.reserveconfirm.ReserveConfirmActivity
import com.artilearn.happygolfgo.util.MyApplication
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : BaseActivity<ActivityHomeBinding>(R.layout.activity_home) {
    private val TAG = javaClass.simpleName

    private lateinit var myApplication: MyApplication
    private lateinit var fragment: Fragment
    private lateinit var bottomNavigation: BottomNavigationView

    private val vm: HomeViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return HomeViewModel() as T
            }
        }
    }

    private val fcmEventType by lazy {
        intent.getStringExtra(FCM_EVENT_TYPE)
    }

    private val reservationType by lazy {
        intent.getStringExtra(RESERVATION_TYPE)
    }

    private val lessonId by lazy {
        intent.getIntExtra(LESSON_ID, 0)
    }

    private val plateId by lazy {
        intent.getIntExtra(PLATE_ID, 0)
    }

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
            bottomNavigationButton.observe(this@HomeActivity, Observer { event ->
                initFragment(event)
            })
            backPressedEvent.observe(this@HomeActivity, Observer {
                showAppFinish()
            })
        }
    }

    private fun initView() {
        initToolbar(binding.homeToolbar.toolbar, getString(R.string.home_toolbar_text), false)
        bottomNavigation = binding.bottomNavigation

        when (fcmEventType) {
            "3" -> goReservaitonPlateFromFCM()
            "4" -> goReservationLessonFromFCM()
            "6" -> goLessonLogFromFCM()
            "" -> "" // 알람화면으로
        }
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
    
    private fun initFragment(type: HomeViewModel.BottomNavigationEvent) {
        when (type) {
            HomeViewModel.BottomNavigationEvent.RESERVATION -> {
                initToolbar(binding.homeToolbar.toolbar, getString(R.string.home_toolbar_text), false)
                fragment = ReservationFragment()
                loadFragment(fragment)
            }
            HomeViewModel.BottomNavigationEvent.GAME_CENTER -> {
                initToolbar(binding.homeToolbar.toolbar, getString(R.string.gamecenter_toolbar_title), false)
                fragment = GameCenterFragment()
                loadFragment(fragment)
            }
            HomeViewModel.BottomNavigationEvent.ALRAM -> {
                initToolbar(binding.homeToolbar.toolbar, getString(R.string.alram_toolbar_title), false)
                fragment = AlramFragment()
                loadFragment(fragment)
            }
            HomeViewModel.BottomNavigationEvent.MY_PAGE -> {
                initToolbar(binding.homeToolbar.toolbar, getString(R.string.mypage_toolbar_title), false)
                fragment = MyPageFragment()
                loadFragment(fragment)
            }
        }
    }

    private fun loadFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val transaction = fragmentManager.beginTransaction()

        when (fragment) {
            is ReservationFragment -> {
                fragmentManager.popBackStack(getString(R.string.home_tag_name_reservation), FragmentManager.POP_BACK_STACK_INCLUSIVE)
                transaction.replace(R.id.frame_layout, fragment, getString(R.string.home_tag_name_reservation))
                transaction.addToBackStack(getString(R.string.home_tag_name_reservation))
            }
            is GameCenterFragment -> {
                fragmentManager.popBackStack(getString(R.string.home_tag_name_gamecenter), FragmentManager.POP_BACK_STACK_INCLUSIVE)
                transaction.replace(R.id.frame_layout, fragment, getString(R.string.home_tag_name_gamecenter))
                transaction.addToBackStack(getString(R.string.home_tag_name_gamecenter))
            }
            is AlramFragment -> {
                fragmentManager.popBackStack(getString(R.string.home_tag_name_alram), FragmentManager.POP_BACK_STACK_INCLUSIVE)
                transaction.replace(R.id.frame_layout, fragment, getString(R.string.home_tag_name_alram))
                transaction.addToBackStack(getString(R.string.home_tag_name_alram))
            }
            is MyPageFragment -> {
                fragmentManager.popBackStack(getString(R.string.home_tag_name_mypage), FragmentManager.POP_BACK_STACK_INCLUSIVE)
                transaction.replace(R.id.frame_layout, fragment, getString(R.string.home_tag_name_mypage))
                transaction.addToBackStack(getString(R.string.home_tag_name_mypage))
            }
        }

        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit()
        transaction.isAddToBackStackAllowed
    }

    private fun backButtonBottomNavigationListener(navigation: BottomNavigationView) {
        val reservation = supportFragmentManager.findFragmentByTag(getString(R.string.home_tag_name_reservation))
        val gamecenter = supportFragmentManager.findFragmentByTag(getString(R.string.home_tag_name_gamecenter))
        val alram = supportFragmentManager.findFragmentByTag(getString(R.string.home_tag_name_alram))
        val mypage = supportFragmentManager.findFragmentByTag(getString(R.string.home_tag_name_mypage))

        when {
            reservation != null && reservation.isVisible -> {
                initToolbar(binding.homeToolbar.toolbar, getString(R.string.home_toolbar_text), false)
                navigation.menu.findItem(R.id.reservation).isChecked = true
            }
            gamecenter != null && gamecenter.isVisible -> {
                initToolbar(binding.homeToolbar.toolbar, getString(R.string.gamecenter_toolbar_title), false)
                navigation.menu.findItem(R.id.gamecenter).isChecked = true
            }
            alram != null && alram.isVisible -> {
                initToolbar(binding.homeToolbar.toolbar, getString(R.string.alram_toolbar_title), false)
                navigation.menu.findItem(R.id.alram).isChecked = true
            }
            mypage != null && mypage.isVisible -> {
                initToolbar(binding.homeToolbar.toolbar, getString(R.string.mypage_toolbar_title), false)
                navigation.menu.findItem(R.id.mypage).isChecked = true
            }
        }
    }

    private fun showAppFinish() {
        val dialog = AlertDialog.Builder(this@HomeActivity)
        dialog.setTitle(getString(R.string.home_app_finish_title))
            .setPositiveButton(getString(R.string.home_app_finish_positive)) { _, _ -> this.finish() }
            .setNegativeButton(getString(R.string.home_app_finish_negative)) { _, _ ->  }
        dialog.create()
        dialog.show()
    }

    private fun goReservaitonPlateFromFCM() {
        val fcmIntent = Intent(this, ReserveConfirmActivity::class.java)
            .apply {
                putExtra(RESERVATION_TYPE, reservationType)
                putExtra(PLATE_ID, plateId)
            }
        startActivity(fcmIntent)
    }

    private fun goReservationLessonFromFCM() {
        val fcmIntent = Intent(this, ReserveConfirmActivity::class.java)
            .apply {
                putExtra(RESERVATION_TYPE, reservationType)
                putExtra(LESSON_ID, lessonId)
            }
        startActivity(fcmIntent)
    }

    private fun goLessonLogFromFCM() {
        val logIntent = Intent(this, LogActivity::class.java)
            .apply {
                putExtra(LESSON_ID, lessonId)
            }
        startActivity(logIntent)
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount == 1) {
            vm.backPressed()
        } else {
            super.onBackPressed()
            backButtonBottomNavigationListener(bottomNavigation)
        }
    }

    companion object {
        const val RESERVATION = "예약"
        const val GAME_CENTER = "림게임센터"
        const val ALRAM = "알림"
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