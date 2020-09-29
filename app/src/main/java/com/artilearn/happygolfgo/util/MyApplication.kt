package com.artilearn.happygolfgo.util

import android.app.Application
import android.content.Context
import com.artilearn.happygolfgo.api.ApiInterface
import com.artilearn.happygolfgo.api.ApiService
import com.artilearn.happygolfgo.data.alram.source.AlramRepository
import com.artilearn.happygolfgo.data.alram.source.AlramRepositoryImpl
import com.artilearn.happygolfgo.data.alram.source.remote.AlramRemoteDataSource
import com.artilearn.happygolfgo.data.alram.source.remote.AlramRemoteDataSourceImpl
import com.artilearn.happygolfgo.data.comment.source.CommentRepository
import com.artilearn.happygolfgo.data.comment.source.CommentRepositoryImpl
import com.artilearn.happygolfgo.data.comment.source.remote.CommentRemoteDataSource
import com.artilearn.happygolfgo.data.comment.source.remote.CommentRemoteDataSourceImpl
import com.artilearn.happygolfgo.data.developeroption.source.DeveloperOptionRepository
import com.artilearn.happygolfgo.data.developeroption.source.DeveloperOptionRepositoryImpl
import com.artilearn.happygolfgo.data.developeroption.source.local.DeveloperOptionDataSoucre
import com.artilearn.happygolfgo.data.developeroption.source.local.DeveloperOptionDataSoucreImpl
import com.artilearn.happygolfgo.data.home.source.HomeRepository
import com.artilearn.happygolfgo.data.home.source.HomeRepositoryImpl
import com.artilearn.happygolfgo.data.home.source.remote.HomeRemoteDataSource
import com.artilearn.happygolfgo.data.home.source.remote.HomeRemoteDataSourceImpl
import com.artilearn.happygolfgo.data.lessonlog.source.LessonLogRepository
import com.artilearn.happygolfgo.data.lessonlog.source.LessonLogRepositoryImpl
import com.artilearn.happygolfgo.data.lessonlog.source.remote.LessonLogRemoteDataSource
import com.artilearn.happygolfgo.data.lessonlog.source.remote.LessonLogRemoteDataSourceImpl
import com.artilearn.happygolfgo.data.log.source.LogRepository
import com.artilearn.happygolfgo.data.log.source.LogRepositoryImpl
import com.artilearn.happygolfgo.data.log.source.remote.LogRemoteDataSource
import com.artilearn.happygolfgo.data.log.source.remote.LogRemoteDataSourceImpl
import com.artilearn.happygolfgo.data.login.source.LoginRepository
import com.artilearn.happygolfgo.data.login.source.LoginRepositoryImpl
import com.artilearn.happygolfgo.data.login.source.local.LoginLocalDataSource
import com.artilearn.happygolfgo.data.login.source.local.LoginLocalDataSourceImpl
import com.artilearn.happygolfgo.data.login.source.remote.LoginRemoteDataSource
import com.artilearn.happygolfgo.data.login.source.remote.LoginRemoteDataSourceImpl
import com.artilearn.happygolfgo.data.myinfo.source.MyInfoRepository
import com.artilearn.happygolfgo.data.myinfo.source.MyInfoRepositoryImpl
import com.artilearn.happygolfgo.data.myinfo.source.local.MyInfoLocalDataSource
import com.artilearn.happygolfgo.data.myinfo.source.local.MyInfoLocalDataSourceImpl
import com.artilearn.happygolfgo.data.myinfo.source.remote.MyInfoRemoteDataSource
import com.artilearn.happygolfgo.data.myinfo.source.remote.MyInfoRemoteDataSourceImpl
import com.artilearn.happygolfgo.data.pwauth.source.PwAuthRepository
import com.artilearn.happygolfgo.data.pwauth.source.PwAuthRepositoryImpl
import com.artilearn.happygolfgo.data.pwauth.source.remote.PwAuthRemoteDataSource
import com.artilearn.happygolfgo.data.pwauth.source.remote.PwAuthRemoteDataSourceImpl
import com.artilearn.happygolfgo.data.pwreset.source.PwrestRepository
import com.artilearn.happygolfgo.data.pwreset.source.PwrestRepositoryImpl
import com.artilearn.happygolfgo.data.pwreset.source.local.PwrestLocalDataSource
import com.artilearn.happygolfgo.data.pwreset.source.local.PwrestLocalDataSourceImpl
import com.artilearn.happygolfgo.data.pwreset.source.remote.PwrestRemoteDataSource
import com.artilearn.happygolfgo.data.pwreset.source.remote.PwrestRemoteDataSourceImpl
import com.artilearn.happygolfgo.data.reserveconfirm.source.CancelRepository
import com.artilearn.happygolfgo.data.reserveconfirm.source.ReservationRepository
import com.artilearn.happygolfgo.data.reserveconfirm.source.ReserveConfirmRepository
import com.artilearn.happygolfgo.data.reserveconfirm.source.ReserveConfirmRepositoryImpl
import com.artilearn.happygolfgo.data.reserveconfirm.source.remote.CancelRemoteDataSource
import com.artilearn.happygolfgo.data.reserveconfirm.source.remote.ReservationRemoteDataSource
import com.artilearn.happygolfgo.data.reserveconfirm.source.remote.ReserveConfirmRemoteDataSource
import com.artilearn.happygolfgo.data.reserveconfirm.source.remote.ReserveConfirmRemoteDataSourceImpl
import com.artilearn.happygolfgo.data.reservelist.source.ReserveListRepository
import com.artilearn.happygolfgo.data.reservelist.source.ReserveListRepositoryImpl
import com.artilearn.happygolfgo.data.reservelist.source.remote.ReserveListRemoteDataSource
import com.artilearn.happygolfgo.data.reservelist.source.remote.ReserveListRemoteDataSourceImpl
import com.artilearn.happygolfgo.data.splash.source.SplashRepository
import com.artilearn.happygolfgo.data.splash.source.SplashRepositoryImpl
import com.artilearn.happygolfgo.data.splash.source.local.SplashLocalDataSource
import com.artilearn.happygolfgo.data.splash.source.local.SplashLocalDataSourceImpl
import com.artilearn.happygolfgo.data.splash.source.remote.SplashRemoteDataSource
import com.artilearn.happygolfgo.data.splash.source.remote.SplashRemoteDataSourceImpl
import com.artilearn.happygolfgo.data.ticketdate.source.TicketDateRepository
import com.artilearn.happygolfgo.data.ticketdate.source.TicketDateRepositoryImpl
import com.artilearn.happygolfgo.data.ticketdate.source.remote.TicketDateRemoteDataSource
import com.artilearn.happygolfgo.data.ticketdate.source.remote.TicketDateRemoteDataSourceImpl
import com.artilearn.happygolfgo.data.tickettime.source.TicketTimeRepository
import com.artilearn.happygolfgo.data.tickettime.source.TicketTimeRepositoryImpl
import com.artilearn.happygolfgo.data.tickettime.source.remote.TicketTimeRemoteDataSource
import com.artilearn.happygolfgo.data.tickettime.source.remote.TicketTimeRemoteDataSourceImpl
import com.artilearn.happygolfgo.data.tutorial.source.TutorialRepository
import com.artilearn.happygolfgo.data.tutorial.source.TutorialRepositoryImpl
import com.artilearn.happygolfgo.data.tutorial.source.local.TutorialLocalDataSource
import com.artilearn.happygolfgo.data.tutorial.source.local.TutorialLocalDataSourceImpl
import com.artilearn.happygolfgo.data.version.source.VersionRepository
import com.artilearn.happygolfgo.data.version.source.VersionRepositoryImpl
import com.artilearn.happygolfgo.data.version.source.local.VersionLocalDataSource
import com.artilearn.happygolfgo.data.version.source.local.VersionLocalDataSourceImpl
import com.artilearn.happygolfgo.data.version.source.remote.VersionRemoteDataSource
import com.artilearn.happygolfgo.data.version.source.remote.VersionRemoteDataSourceImpl
import com.crashlytics.android.Crashlytics
import com.facebook.stetho.Stetho
import com.google.firebase.iid.FirebaseInstanceId
import io.fabric.sdk.android.Fabric

class MyApplication : Application() {

    private lateinit var networkManager: NetworkManager
    private lateinit var apiInterface: ApiInterface
    private lateinit var preferenceManager: PreferenceManager
    private lateinit var firebaseInstanceId: FirebaseInstanceId

    private lateinit var loginRemoteDataSource: LoginRemoteDataSource
    private lateinit var loginLocalDataSource: LoginLocalDataSource
    private lateinit var splashLocalDataSource: SplashLocalDataSource
    private lateinit var splashRemoteDataSource: SplashRemoteDataSource
    private lateinit var tutorialLocalDataSource: TutorialLocalDataSource
    private lateinit var homeRemoteDataSource: HomeRemoteDataSource
    private lateinit var versionRemoteDataSource: VersionRemoteDataSource
    private lateinit var versionLocalDataSource: VersionLocalDataSource
    private lateinit var myinfoLocalDataSource: MyInfoLocalDataSource
    private lateinit var myinfoRemoteDataSource: MyInfoRemoteDataSource
    private lateinit var pwAuthRemoteDataSource: PwAuthRemoteDataSource
    private lateinit var pwResetRemoteDataSource: PwrestRemoteDataSource
    private lateinit var pwResetLocalDataSource: PwrestLocalDataSource
    private lateinit var lessonLogRemoteDataSource: LessonLogRemoteDataSource
    private lateinit var alramRemoteDataSource: AlramRemoteDataSource
    private lateinit var ticketDateRemoteDataSource: TicketDateRemoteDataSource
    private lateinit var ticketTimeRemoteDataSource: TicketTimeRemoteDataSource
    private lateinit var reserveConfirmRemoteDataSource: ReserveConfirmRemoteDataSource
    private lateinit var reservationRemoteDataSource: ReservationRemoteDataSource
    private lateinit var reserveListRemoteDataSource: ReserveListRemoteDataSource
    private lateinit var cancelRemoteDataSource: CancelRemoteDataSource
    private lateinit var logRemoteDataSource: LogRemoteDataSource
    private lateinit var commentRemoteDataSource: CommentRemoteDataSource
    private lateinit var developerOptionDataSoucre: DeveloperOptionDataSoucre

    lateinit var loginRepository: LoginRepository
    lateinit var splashRepository: SplashRepository
    lateinit var tutorialRepository: TutorialRepository
    lateinit var homeRepository: HomeRepository
    lateinit var versionRepository: VersionRepository
    lateinit var myinfoRepository: MyInfoRepository
    lateinit var pwAuthRepository: PwAuthRepository
    lateinit var pwrestRepository: PwrestRepository
    lateinit var lessonLogRepository: LessonLogRepository
    lateinit var alramRepository: AlramRepository
    lateinit var ticketDateRepository: TicketDateRepository
    lateinit var ticketTimeRepository: TicketTimeRepository
    lateinit var reserveConfirmRepository: ReserveConfirmRepository
    lateinit var reservationRepository: ReservationRepository
    lateinit var reserveListRepository: ReserveListRepository
    lateinit var cancelRepository: CancelRepository
    lateinit var logRespository: LogRepository
    lateinit var commentRepository: CommentRepository
    lateinit var developerOptionRepository: DeveloperOptionRepository

    override fun onCreate() {
        super.onCreate()

        Stetho.initializeWithDefaults(this)
        inject()
        setCrashHandler()
    }

    private fun inject() {
        networkManager = NetworkManager(this)
        apiInterface = ApiService.api
        preferenceManager = PreferenceManager(this)
        firebaseInstanceId = FirebaseInstanceId.getInstance()

        loginRemoteDataSource = LoginRemoteDataSourceImpl(apiInterface)
        loginLocalDataSource = LoginLocalDataSourceImpl(preferenceManager)
        splashLocalDataSource = SplashLocalDataSourceImpl(preferenceManager)
        splashRemoteDataSource = SplashRemoteDataSourceImpl(apiInterface, firebaseInstanceId)
        tutorialLocalDataSource = TutorialLocalDataSourceImpl(preferenceManager)
        homeRemoteDataSource = HomeRemoteDataSourceImpl(apiInterface)
        versionRemoteDataSource = VersionRemoteDataSourceImpl(apiInterface)
        versionLocalDataSource = VersionLocalDataSourceImpl(preferenceManager)
        myinfoLocalDataSource = MyInfoLocalDataSourceImpl(preferenceManager)
        myinfoRemoteDataSource = MyInfoRemoteDataSourceImpl(apiInterface)
        pwAuthRemoteDataSource = PwAuthRemoteDataSourceImpl(apiInterface)
        pwResetRemoteDataSource = PwrestRemoteDataSourceImpl(apiInterface)
        pwResetLocalDataSource = PwrestLocalDataSourceImpl(preferenceManager)
        lessonLogRemoteDataSource = LessonLogRemoteDataSourceImpl(apiInterface)
        alramRemoteDataSource = AlramRemoteDataSourceImpl(apiInterface)
        ticketDateRemoteDataSource = TicketDateRemoteDataSourceImpl(apiInterface)
        ticketTimeRemoteDataSource = TicketTimeRemoteDataSourceImpl(apiInterface)
        reserveConfirmRemoteDataSource = ReserveConfirmRemoteDataSourceImpl(apiInterface)
        reservationRemoteDataSource = ReserveConfirmRemoteDataSourceImpl(apiInterface)
        reserveListRemoteDataSource = ReserveListRemoteDataSourceImpl(apiInterface)
        cancelRemoteDataSource = ReserveConfirmRemoteDataSourceImpl(apiInterface)
        logRemoteDataSource = LogRemoteDataSourceImpl(apiInterface)
        commentRemoteDataSource = CommentRemoteDataSourceImpl(apiInterface)
        developerOptionDataSoucre = DeveloperOptionDataSoucreImpl(preferenceManager)

        loginRepository = LoginRepositoryImpl(loginLocalDataSource, loginRemoteDataSource, networkManager)
        splashRepository = SplashRepositoryImpl(splashLocalDataSource, splashRemoteDataSource, preferenceManager, networkManager)
        tutorialRepository = TutorialRepositoryImpl(tutorialLocalDataSource)
        homeRepository = HomeRepositoryImpl(homeRemoteDataSource, networkManager)
        versionRepository = VersionRepositoryImpl(versionRemoteDataSource, versionLocalDataSource, preferenceManager, networkManager)
        myinfoRepository = MyInfoRepositoryImpl(myinfoLocalDataSource, myinfoRemoteDataSource, networkManager)
        pwAuthRepository = PwAuthRepositoryImpl(pwAuthRemoteDataSource, networkManager)
        pwrestRepository = PwrestRepositoryImpl(pwResetRemoteDataSource, pwResetLocalDataSource, networkManager)
        lessonLogRepository = LessonLogRepositoryImpl(lessonLogRemoteDataSource, networkManager)
        alramRepository = AlramRepositoryImpl(alramRemoteDataSource, networkManager)
        ticketDateRepository = TicketDateRepositoryImpl(ticketDateRemoteDataSource, networkManager)
        ticketTimeRepository = TicketTimeRepositoryImpl(ticketTimeRemoteDataSource, networkManager)
        reserveConfirmRepository = ReserveConfirmRepositoryImpl(reserveConfirmRemoteDataSource, reservationRemoteDataSource, cancelRemoteDataSource, networkManager)
        reservationRepository = ReserveConfirmRepositoryImpl(reserveConfirmRemoteDataSource, reservationRemoteDataSource, cancelRemoteDataSource, networkManager)
        reserveListRepository = ReserveListRepositoryImpl(reserveListRemoteDataSource, networkManager)
        cancelRepository = ReserveConfirmRepositoryImpl(reserveConfirmRemoteDataSource, reservationRemoteDataSource, cancelRemoteDataSource, networkManager)
        logRespository = LogRepositoryImpl(logRemoteDataSource, networkManager)
        commentRepository = CommentRepositoryImpl(commentRemoteDataSource, networkManager)
        developerOptionRepository = DeveloperOptionRepositoryImpl(developerOptionDataSoucre)
    }

    init {
        instance = this
    }

    companion object {
        private var instance: MyApplication? = null

        fun getApplicationContext(): Context {
            return instance!!.applicationContext
        }
    }

    private fun setCrashHandler() {
        val defaultExceptionHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler { _, _ ->
            // Crashlytics에서 기본 handler를 호출하기 때문에 이중으로 호출되는것을 막기위해 빈 handler로 설정
        }
        Fabric.with(this, Crashlytics())
        val fabricExceptionHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler(
            HappyGolfUserExceptionHandler(
                this,
                defaultExceptionHandler,
                fabricExceptionHandler
            )
        )
    }

}