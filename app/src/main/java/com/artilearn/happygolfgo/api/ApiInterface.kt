package com.artilearn.happygolfgo.api

import com.artilearn.happygolfgo.data.alram.AlramResponse
import com.artilearn.happygolfgo.data.comment.CommentModel
import com.artilearn.happygolfgo.data.home.HomeResponse
import com.artilearn.happygolfgo.data.lessonlog.LessonLogResponse
import com.artilearn.happygolfgo.data.log.LogResponse
import com.artilearn.happygolfgo.data.login.LoginDataModel
import com.artilearn.happygolfgo.data.login.LoginResponse
import com.artilearn.happygolfgo.data.myinfo.MyInfoModel
import com.artilearn.happygolfgo.data.myinfo.MyInfoResponse
import com.artilearn.happygolfgo.data.pwauth.ConfirmAuthModel
import com.artilearn.happygolfgo.data.pwauth.GetAuthModel
import com.artilearn.happygolfgo.data.pwreset.PwrestModel
import com.artilearn.happygolfgo.data.reserveconfirm.*
import com.artilearn.happygolfgo.data.reservelist.ReserveListResponse
import com.artilearn.happygolfgo.data.splash.SplashTokenModel
import com.artilearn.happygolfgo.data.splash.SplashTokenResponse
import com.artilearn.happygolfgo.data.splash.SynchronizedDataModel
import com.artilearn.happygolfgo.data.splash.SynchronizedResponse
import com.artilearn.happygolfgo.data.ticketdate.TicketDateResponse
import com.artilearn.happygolfgo.data.tickettime.TicketTimeResponse
import com.artilearn.happygolfgo.data.version.VersionResponse
import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.*

interface ApiInterface {
    @POST("auth/user/doSignin")
    fun login(
        @Body data: HashMap<String, LoginDataModel>
    ): Single<LoginResponse>

    @POST("auth/common/renewAccessToken")
    fun getAccessToken(
        @Body data: HashMap<String, SplashTokenModel>
    ): Single<Response<SplashTokenResponse>>

    @POST("auth/common/update")
    fun userSynchronized(
        @Body data: HashMap<String, SynchronizedDataModel>
    ): Single<Response<SynchronizedResponse>>

    @GET("main/user")
    fun mainHome(): Single<Response<HomeResponse>>

    @GET("mypage/appversion")
    fun getAppVersion(
        @Query("platform") platform: Int = 1,
        @Query("clientType") clientType: Int = 0
    ): Single<VersionResponse>

    @PUT("mypage/profile")
    fun updateProfile(
        @Body data: HashMap<String, MyInfoModel>
    ): Single<MyInfoResponse>

    @POST("auth/common/verification/send")
    fun getAuthNumber(
        @Body data: HashMap<String, GetAuthModel>
    ): Completable

    @POST("auth/common/verification/check")
    fun confirmAuthNumber(
        @Body data: HashMap<String, ConfirmAuthModel>
    ): Completable

    @PUT("auth/user/changePassword")
    fun changePassword(
        @Body data: HashMap<String, PwrestModel>
    ): Completable

    @GET("user/lessonlogs")
    fun getLessonLogs(): Single<Response<LessonLogResponse>>

    @GET("main/user/notifications")
    fun getAlramMessage(): Single<Response<AlramResponse>>

    @GET("reservation/user/plateAvailableDates/{ticketID}")
    fun selectPlateDate(
        @Path("ticketID") ticketId: Int
    ): Single<Response<TicketDateResponse>>

    @GET("reservation/user/lessonAvailableDates/{ticketID}")
    fun selectLessonDate(
        @Path("ticketID") ticketId: Int
    ): Single<Response<TicketDateResponse>>

    @GET("reservation/user/plateAvailability")
    fun selectPlateTime(
        @Query("startDate") startDate: String,
        @Query("ticketID") ticketId: Int
    ): Single<Response<TicketTimeResponse>>

    @GET("reservation/user/lessonAvailability")
    fun selectLessonTime(
        @Query("startDate") startDate: String,
        @Query("ticketID") ticketId: Int
    ): Single<Response<TicketTimeResponse>>

    @GET("reservation/user/plate/policy")
    fun requestPolicy(): Single<Response<PolicyResponse>>

    @POST("reservation/user/plate")
    fun reservationPlate(
        @Body data: HashMap<String, ReservePlateModel>
    ): Single<Response<ReservePlateResponse>>

    @POST("reservation/user/lessonReservation")
    fun reservationLesson(
        @Body data: HashMap<String, ReserveLessonModel>
    ): Single<Response<ReserveLessonResponse>>

    @GET("reservation/user/myReservations")
    fun reservationList(): Single<Response<ReserveListResponse>>

    @DELETE("reservation/user/lessonReservation/{lessonReservationID}")
    fun reservationLessonCancel(
        @Path("lessonReservationID") lessonId: Int
    ): Completable

    @DELETE("reservation/user/plateReservation/{plateReservationID}")
    fun reservationPlateCancel(
        @Path("plateReservationID") plateId: Int
    ): Completable

    @GET("user/lessonlogs/detail/{id}")
    fun lessonLogDetail(
        @Path("id") lessonLogId: Int
    ): Single<Response<LogResponse>>

    @PUT("user/lessonlogs")
    fun uploadLessonComment(
        @Body data: HashMap<String, CommentModel>
    ): Completable

    @GET("reservation/user/lessonReservation/detail/{lessonReservationID}")
    fun getReservationLessonDetail(
        @Path("lessonReservationID") lessonId: Int
    ): Single<Response<ReservationLessonDetail>>

    @GET("reservation/user/plateReservation/detail/{plateReservationID}")
    fun getReservationPlateDetail(
        @Path("plateReservationID") plateId: Int
    ): Single<Response<ReservationPlateDetail>>

    @PUT("mypage/signOut")
    fun userLogout(): Completable

    @POST("reservation/user/plateReservation/changeTime")
    fun plateTimeChange(
        @Body data: HashMap<String, PlateTimeChangeModel>
    ): Single<Response<PlateTimeChangeResponse>>
}