package com.artilearn.happygolfgo.modules

import com.artilearn.happygolfgo.ui.comment.CommentViewModel
import com.artilearn.happygolfgo.ui.common.DeveloperOptionViewModel
import com.artilearn.happygolfgo.ui.golfgame.GolfGameViewModel
import com.artilearn.happygolfgo.ui.golfrank.GolfRankViewModel
import com.artilearn.happygolfgo.ui.home.HomeViewModel
import com.artilearn.happygolfgo.ui.home.alram.AlramViewModel
import com.artilearn.happygolfgo.ui.home.gamecenter.GameCenterViewModel
import com.artilearn.happygolfgo.ui.home.mypage.MyPageViewModel
import com.artilearn.happygolfgo.ui.home.mypage.profilechange.ProfileChangeViewModel
import com.artilearn.happygolfgo.ui.home.reservation.ReservationViewModel
import com.artilearn.happygolfgo.ui.lessonpostlist.LessonPostListViewModel
import com.artilearn.happygolfgo.ui.log.LogViewModel
import com.artilearn.happygolfgo.ui.login.LoginViewModel
import com.artilearn.happygolfgo.ui.myinfo.MyInfoViewModel
import com.artilearn.happygolfgo.ui.pwauth.PwAuthViewModel
import com.artilearn.happygolfgo.ui.pwreset.PwResetViewModel
import com.artilearn.happygolfgo.ui.reserveconfirm.ReserveConfirmViewModel
import com.artilearn.happygolfgo.ui.reservelist.ReserveListViewModel
import com.artilearn.happygolfgo.ui.reservelist.fragment.ReserveLessonViewModel
import com.artilearn.happygolfgo.ui.reservelist.fragment.ReservePlateViewModel
import com.artilearn.happygolfgo.ui.splash.SplashViewModel
import com.artilearn.happygolfgo.ui.ticketdate.TicketDateViewModel
import com.artilearn.happygolfgo.ui.ticketmanager.TicketManagerViewModel
import com.artilearn.happygolfgo.ui.tickettime.TicketTimeViewModel
import com.artilearn.happygolfgo.ui.tutorial.TutorialViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel { TutorialViewModel(get()) }

    viewModel { TicketTimeViewModel(get()) }

    viewModel { TicketDateViewModel(get()) }

    viewModel { SplashViewModel(get(), get()) }

    viewModel { ReserveListViewModel(get()) }

    viewModel { ReservePlateViewModel() }

    viewModel { ReserveLessonViewModel() }

    viewModel { ReserveConfirmViewModel(get(), get(), get(), get()) }

    viewModel { PwResetViewModel(get()) }

    viewModel { PwAuthViewModel(get()) }

    viewModel { MyPageViewModel(get(), get()) }

    viewModel { MyInfoViewModel(get()) }

    viewModel { LoginViewModel(get(), get()) }

    viewModel { LogViewModel(get()) }

    viewModel { LessonPostListViewModel(get()) }

    viewModel { HomeViewModel() }

    viewModel { ReservationViewModel(get(), get()) }

    viewModel { GameCenterViewModel(get()) }

    viewModel { AlramViewModel(get()) }

    viewModel { GolfRankViewModel(get()) }

    viewModel { CommentViewModel(get()) }

    viewModel { DeveloperOptionViewModel(get()) }

    viewModel { GolfGameViewModel(get()) }

    viewModel { TicketManagerViewModel() }

    viewModel { ProfileChangeViewModel(get()) }
}