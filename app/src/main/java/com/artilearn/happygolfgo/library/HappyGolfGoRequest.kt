package com.artilearn.happygolfgo.library

import com.tlsolution.tlsmodules.Network.Request
import com.tlsolution.tlsmodules.Network.RequestEnvironmentMode

enum class HappyGolfGoRequest : Request {
    clientSignIn,
    main,
    plateTime,
    plate,
    makePlateReservation,
    reservationList,
    cancelPlateReservation,
    lessonTime,
    plateTimeAfterLessonTime,
    makeLessonReservation,
    cancelLessonReservation,
    clientSignOut,
    selectPlateDate,
    selectLessonDate,
    selectPlateTime,
    selectLessonTime,
    selectPlate,
    selectPlateReserve,
    userSynchronized,
    pushAlram,
    pwNumber,
    verifyNumber,
    pwUpdate,
    nickNameUpdate,
    withOutPlateNumberReserve,
    appVersion,
    lessonReviewList,
    lessonReviewDetail,
    uploadLessonReview,
    lessonDetail,
    plateDetail,
    ;


    override var endpoint: String = ""
    override var baseURL: String = ""

    override fun getBaseURLString(): String {
        return when (environmentMode) {
            RequestEnvironmentMode.production -> "https://happygolfgoapi.com/happygolfgo/appClient/"
            RequestEnvironmentMode.test -> "http://172.30.1.17:8080/appClient/"
            RequestEnvironmentMode.development -> "http://52.79.253.238/development/appClient/"
        }
    }

    override var url: String? = null
        get() {
            when (this) {
                main -> return getBaseURLString() + "main/user"
                selectPlateDate -> return getBaseURLString() + "reservation/user/plateAvailableDates/${endpoint}"
                selectLessonDate -> return getBaseURLString() + "reservation/user/lessonAvailableDates/${endpoint}"
                selectPlateTime -> return getBaseURLString() + "reservation/user/plateAvailability?${endpoint}"
                selectLessonTime -> return getBaseURLString() + "reservation/user/lessonAvailability?${endpoint}"
                cancelPlateReservation -> return getBaseURLString() + "reservation/user/plateReservation/${endpoint}"
                cancelLessonReservation -> return getBaseURLString() + "reservation/user/lessonReservation/${endpoint}"
                selectPlate -> return getBaseURLString() + "reservation/user/availablePlates/${endpoint}"
                selectPlateReserve -> return getBaseURLString() + "reservation/user/plateReservation/detail/${endpoint}"
                makePlateReservation -> return getBaseURLString() + "reservation/user/plateReservation"
                makeLessonReservation -> return getBaseURLString() + "reservation/user/lessonReservation"
                userSynchronized -> return getBaseURLString() + "auth/common/uptodate"
                clientSignOut -> return getBaseURLString() + "mypage/signOut"
                clientSignIn -> return getBaseURLString() + "auth/user/signIn"
                pushAlram -> return getBaseURLString() + "main/user/notifications"
                pwNumber -> return getBaseURLString() + "auth/common/verification/send"
                verifyNumber -> return getBaseURLString() + "auth/common/verification/check"
                pwUpdate -> return getBaseURLString() + "auth/user/changePassword"
                nickNameUpdate -> return getBaseURLString() + "mypage/profile"
                withOutPlateNumberReserve -> return getBaseURLString() + "reservation/user/plate"
                appVersion -> return getBaseURLString() + "mypage/appversion?platform=1&clientType=0"
                lessonReviewList -> return getBaseURLString() + "user/lessonlogs"
                lessonReviewDetail -> return getBaseURLString() + "user/lessonlogs/detail/${endpoint}"
                uploadLessonReview -> return getBaseURLString() + "user/lessonlogs"
                lessonDetail -> return getBaseURLString() + "reservation/user/lessonReservation/detail/${endpoint}"
                plateDetail -> return getBaseURLString() + "reservation/user/plateReservation/detail/${endpoint}"

                plateTime -> return getBaseURLString() + "reservation/plateReservation/plateAvailability?${endpoint}"
                plate -> return getBaseURLString() + "reservation/plateReservation/${endpoint}/relatedPlate"
                reservationList -> return getBaseURLString() + "reservation/user/myReservations"
                lessonTime -> return getBaseURLString() + "reservation/lessonReservation/lessonAvailability?${endpoint}"
                plateTimeAfterLessonTime -> return getBaseURLString() + "reservation/lessonReservation/plateAvailability?${endpoint}"

            }
        }

    override var body: String? = null

    override var method: String? = null
        get() {
            when (this) {
                clientSignIn,
                makePlateReservation,
                makeLessonReservation,
                pwNumber,
                verifyNumber,
                withOutPlateNumberReserve,
                userSynchronized -> return "POST"
                main,
                plateTime,
                plate,
                reservationList,
                lessonTime,
                selectPlateDate,
                selectLessonDate,
                selectPlateTime,
                selectLessonTime,
                selectPlate,
                selectPlateReserve,
                pushAlram,
                appVersion,
                lessonReviewList,
                lessonReviewDetail,
                lessonDetail,
                plateDetail,
                plateTimeAfterLessonTime -> return "GET"

                clientSignOut,
                nickNameUpdate,
                uploadLessonReview,
                pwUpdate -> return "PUT"

                cancelPlateReservation,
                cancelLessonReservation -> return "DELETE"
            }
        }

    override var tokenURL: String = baseURL + "auth/common/token"
    override var environmentMode: RequestEnvironmentMode = RequestEnvironmentMode.production
}

