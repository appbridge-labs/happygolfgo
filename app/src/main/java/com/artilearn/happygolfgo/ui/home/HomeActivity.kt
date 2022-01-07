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
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeActivity : BaseActivity<ActivityHomeBinding>(R.layout.activity_home) {
    private val vm: HomeViewModel by viewModel()

    private lateinit var reservationFragment: ReservationFragment
    private lateinit var alramFragment: AlramFragment
    private lateinit var myPageFragment: MyPageFragment

    private val fcmEventType by lazy { intent.getStringExtra(FCM_EVENT_TYPE) }
    private val reservationType by lazy { intent.getStringExtra(RESERVATION_TYPE) }
    private val lessonId by lazy { intent.getIntExtra(LESSON_ID, 0) }
    private val plateId by lazy { intent.getIntExtra(PLATE_ID, 0) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initBinding()
        initViewModelObserving()
        setSupportActionBar((binding.homeToolbar.toolbar))
    }

    private fun initBinding() {
        binding.vm = vm
    }

    private fun initViewModelObserving() {
        with(vm) {
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
            bottom.observe(this@HomeActivity, Observer {
                toolbarText(it)
                initFragment(it)
            })
        }
    }

    private fun toolbarText(name: String) {
        var title = getString(R.string.home_toolbar_text)

        when (name) {
            "예약" ->  title = getString(R.string.home_toolbar_text)
            "알림" -> title = getString(R.string.alram_toolbar_title)
            "마이페이지" -> title = getString(R.string.mypage_toolbar_title)
        }

        initToolbar(binding.homeToolbar.toolbar, title, false)
    }

    private fun initFragment(name: String) {
        when (name) {
            "예약" -> {
                reservationFragment = ReservationFragment()
                loadFragment(reservationFragment)
            }
            "알림" -> {
                alramFragment = AlramFragment()
                loadFragment(alramFragment)
            }
            "마이페이지" -> {
                myPageFragment = MyPageFragment()
                loadFragment(myPageFragment)
            }
        }
    }

    private fun menuIsCheckItem(name: String) {
        with(binding.bottomNavigation) {
            when (name) {
                "예약" -> menu.findItem(R.id.reservation).isChecked = true
                //"게임센터" -> menu.findItem(R.id.gamecenter).isChecked = true
                "알림" -> menu.findItem(R.id.alram).isChecked = true
                "마이페이지" -> menu.findItem(R.id.mypage).isChecked = true
            }
        }
    }

    private fun loadFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        var tagName = ""
        when (fragment) {
            is ReservationFragment -> tagName = "예약"
            is AlramFragment -> tagName = "알림"
            is MyPageFragment -> tagName = "마이페이지"
        }

        menuIsCheckItem(tagName)
        fragmentManager.popBackStack(tagName, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        transaction.apply {
            replace(R.id.frame_layout, fragment, tagName)
            addToBackStack(tagName)
            setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit()
            isAddToBackStackAllowed
        }
    }

    private fun backButtonBottomNavigationListener() {
        val reservation = supportFragmentManager.findFragmentByTag(getString(R.string.home_tag_name_reservation))
        val alram = supportFragmentManager.findFragmentByTag(getString(R.string.home_tag_name_alram))
        val mypage = supportFragmentManager.findFragmentByTag(getString(R.string.home_tag_name_mypage))

        when {
            reservation != null && reservation.isVisible -> {
                toolbarText("예약")
                menuIsCheckItem("예약")
            }
            alram != null && alram.isVisible -> {
                toolbarText("알림")
                menuIsCheckItem("알림")
            }
            mypage != null && mypage.isVisible -> {
                toolbarText("마이페이지")
                menuIsCheckItem("마이페이지")
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
        vm.onNextBottomEvent(getString(R.string.alram_toolbar_title))
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
        //feature/20211222/migration
        //Smart cas to 'String' is impossible, beacause 'fcmEventType' is a property that has open or custom getter
        //so, save it to local temp variable and use it
        when (val  typedFcmEventType : String? =  fcmEventType) {
            "3", "4", "6" -> vm.fcmOnNext(typedFcmEventType)
            "" -> goAlramFCM()
        }
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount == 1) {
            vm.backPressed()
        } else {
            super.onBackPressed()
            backButtonBottomNavigationListener()
            vm.onNextBottomEvent("음")
        }
    }

}