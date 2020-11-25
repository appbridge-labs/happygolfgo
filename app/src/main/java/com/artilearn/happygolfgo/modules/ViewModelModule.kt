package com.artilearn.happygolfgo.modules

import com.artilearn.happygolfgo.ui.comment.CommentViewModel
import com.artilearn.happygolfgo.ui.common.DeveloperOptionViewModel
import com.artilearn.happygolfgo.ui.gamerank.GameRankViewModel
import com.artilearn.happygolfgo.ui.gamescore.GameScoreViewModel
import com.artilearn.happygolfgo.ui.home.HomeViewModel
import com.artilearn.happygolfgo.ui.home.alram.AlramViewModel
import com.artilearn.happygolfgo.ui.home.gamecenter.GameCenterViewModel
import com.artilearn.happygolfgo.ui.home.mypage.MyPageViewModel
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

    viewModel { ReserveConfirmViewModel(get(), get(), get()) }

    viewModel { PwResetViewModel(get()) }

    viewModel { PwAuthViewModel(get()) }

    viewModel { MyPageViewModel(get(), get()) }

    viewModel { MyInfoViewModel(get()) }

    viewModel { LoginViewModel(get(), get()) }

    viewModel { LogViewModel(get()) }

    viewModel { LessonPostListViewModel(get()) }

    viewModel { HomeViewModel() }

    viewModel { ReservationViewModel(get(), get()) }

    viewModel { GameCenterViewModel() }

    viewModel { AlramViewModel(get()) }

    viewModel { GameScoreViewModel() }

    viewModel { GameRankViewModel() }

    viewModel { CommentViewModel(get()) }

    viewModel { AllGroupViewModel() }

    viewModel { MyGroupViewModel() }

    viewModel { DeveloperOptionViewModel(get()) }

}