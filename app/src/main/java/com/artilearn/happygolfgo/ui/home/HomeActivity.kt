package com.artilearn.happygolfgo.ui.home

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.base.BaseActivity
import com.artilearn.happygolfgo.constants.FCM_EVENT_TYPE
import com.artilearn.happygolfgo.constants.LESSON_ID
import com.artilearn.happygolfgo.constants.PLATE_ID
import com.artilearn.happygolfgo.constants.RESERVATION_TYPE
import com.artilearn.happygolfgo.databinding.ActivityHomeBinding
import com.artilearn.happygolfgo.ui.home.alram.AlramFragment
import com.artilearn.happygolfgo.ui.home.mypage.MyPageFragment
import com.artilearn.happygolfgo.ui.home.reservation.ReservationFragment
import com.artilearn.happygolfgo.ui.log.LogActivity
import com.artilearn.happygolfgo.ui.reserveconfirm.ReserveConfirmActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeActivity : BaseActivity<ActivityHomeBinding>(R.layout.activity_home) {

    private val vm: HomeViewModel by viewModel()
    private lateinit var fragment: Fragment
    private lateinit var bottomNavigation: BottomNavigationView

    private val fcmEventType by lazy { intent.getStringExtra(FCM_EVENT_TYPE) }
    private val reservationType by lazy { intent.getStringExtra(RESERVATION_TYPE) }
    private val lessonId by lazy { intent.getIntExtra(LESSON_ID, 0) }
    private val plateId by lazy { intent.getIntExtra(PLATE_ID, 0) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initBinding()
        initViewModelObserving()
        initView()
        bottomNavigationListener()
        setSupportActionBar((binding.homeToolbar.toolbar))
    }

    private fun initBinding() {
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
            eventType.observe(this@HomeActivity, Observer {
                when (eventType.value) {
                    HomeViewModel.FcmTypeMoveModel.PLATE -> goReservaitonPlateFromFCM()
                    HomeViewModel.FcmTypeMoveModel.LESSON -> goReservationLessonFromFCM()
                    HomeViewModel.FcmTypeMoveModel.LOG -> goLessonLogFromFCM()
                }
            })
        }
    }

    private fun initView() {
        initToolbar(binding.homeToolbar.toolbar, getString(R.string.home_toolbar_text), false)
        bottomNavigation = binding.bottomNavigation
    }

    private fun bottomNavigationListener() {
        bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.reservation -> { vm.bottomNavigation(RESERVATION) }
                //R.id.gamecenter -> { vm.bottomNavigation(GAME_CENTER) }
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
//            HomeViewModel.BottomNavigationEvent.GAME_CENTER -> {
//                initToolbar(binding.homeToolbar.toolbar, getString(R.string.gamecenter_toolbar_title), false)
//                fragment = GameCenterFragment()
//                loadFragment(fragment)
//            }
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
                menuIsCheckItem("예약")
                fragmentManager.popBackStack(getString(R.string.home_tag_name_reservation), FragmentManager.POP_BACK_STACK_INCLUSIVE)
                transaction.replace(R.id.frame_layout, fragment, getString(R.string.home_tag_name_reservation))
                transaction.addToBackStack(getString(R.string.home_tag_name_reservation))
            }
//            is GameCenterFragment -> {
//                menuIsCheckItem("게임센터")
//                fragmentManager.popBackStack(getString(R.string.home_tag_name_gamecenter), FragmentManager.POP_BACK_STACK_INCLUSIVE)
//                transaction.replace(R.id.frame_layout, fragment, getString(R.string.home_tag_name_gamecenter))
//                transaction.addToBackStack(getString(R.string.home_tag_name_gamecenter))
//            }
            is AlramFragment -> {
                menuIsCheckItem("알림")
                fragmentManager.popBackStack(getString(R.string.home_tag_name_alram), FragmentManager.POP_BACK_STACK_INCLUSIVE)
                transaction.replace(R.id.frame_layout, fragment, getString(R.string.home_tag_name_alram))
                transaction.addToBackStack(getString(R.string.home_tag_name_alram))
            }
            is MyPageFragment -> {
                menuIsCheckItem("마이페이지")
                fragmentManager.popBackStack(getString(R.string.home_tag_name_mypage), FragmentManager.POP_BACK_STACK_INCLUSIVE)
                transaction.replace(R.id.frame_layout, fragment, getString(R.string.home_tag_name_mypage))
                transaction.addToBackStack(getString(R.string.home_tag_name_mypage))
            }
        }

        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit()
        transaction.isAddToBackStackAllowed
    }

    private fun backButtonBottomNavigationListener() {
        val reservation = supportFragmentManager.findFragmentByTag(getString(R.string.home_tag_name_reservation))
        //val gamecenter = supportFragmentManager.findFragmentByTag(getString(R.string.home_tag_name_gamecenter))
        val alram = supportFragmentManager.findFragmentByTag(getString(R.string.home_tag_name_alram))
        val mypage = supportFragmentManager.findFragmentByTag(getString(R.string.home_tag_name_mypage))

        when {
            reservation != null && reservation.isVisible -> {
                initToolbar(binding.homeToolbar.toolbar, getString(R.string.home_toolbar_text), false)
                menuIsCheckItem("예약")
            }
//            gamecenter != null && gamecenter.isVisible -> {
//                initToolbar(binding.homeToolbar.toolbar, getString(R.string.gamecenter_toolbar_title), false)
//                menuIsCheckItem("게임센터")
//            }
            alram != null && alram.isVisible -> {
                initToolbar(binding.homeToolbar.toolbar, getString(R.string.alram_toolbar_title), false)
                menuIsCheckItem("알림")
            }
            mypage != null && mypage.isVisible -> {
                initToolbar(binding.homeToolbar.toolbar, getString(R.string.mypage_toolbar_title), false)
                menuIsCheckItem("마이페이지")
            }
        }
    }

    private fun menuIsCheckItem(name: String) {
        with(bottomNavigation) {
            when (name) {
                "예약" -> menu.findItem(R.id.reservation).isChecked = true
                //"게임센터" -> menu.findItem(R.id.gamecenter).isChecked = true
                "알림" -> menu.findItem(R.id.alram).isChecked = true
                "마이페이지" -> menu.findItem(R.id.mypage).isChecked = true
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

    private fun goAlramFCM() {
        vm.bottomNavigation(ALRAM)
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

    override fun onStart() {
        super.onStart()

        when (fcmEventType) {
            "3", "4", "6" -> vm.fcmOnNext(fcmEventType)
            "" -> goAlramFCM()
        }
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount == 1) {
            vm.backPressed()
        } else {
            super.onBackPressed()
            backButtonBottomNavigationListener()
        }
    }

    companion object {
        const val RESERVATION = "예약"
        const val GAME_CENTER = "게임센터"
        const val ALRAM = "알림"
        const val MY_PAGE = "마이페이지"
    }

}