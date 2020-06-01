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

    coachSignIn,
    coachMain,
    coachReservation,
    customerInfo;



    override var endpoint: String = ""
    override var baseURL: String = ""

    override fun getBaseURLString(): String {
        return when (environmentMode) {
            RequestEnvironmentMode.production -> "https://happygolfgoapi.com/happygolfgo/appClient/"
            RequestEnvironmentMode.test -> "http://192.168.0.87:8080/appClient/"
            RequestEnvironmentMode.development -> "http://192.168.0.87:8080/appClient/"
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
                userSynchronized -> return getBaseURLString() + "auth/common/update"
                clientSignOut -> return getBaseURLString() + "mypage/signOut"
                clientSignIn -> return getBaseURLString() + "auth/user/signIn"
                pushAlram -> return getBaseURLString() + "main/user/notifications"
                pwNumber -> return getBaseURLString() + "auth/user/sendSMS"
                verifyNumber -> return getBaseURLString() + "auth/user/verifySMS"
                pwUpdate -> return getBaseURLString() + "auth/user/changePassword"
                nickNameUpdate -> return getBaseURLString() + "mypage/profile"

                plateTime -> return getBaseURLString() + "reservation/plateReservation/plateAvailability?${endpoint}"
                plate -> return getBaseURLString() + "reservation/plateReservation/${endpoint}/relatedPlate"
                reservationList -> return getBaseURLString() + "reservation/user"
                lessonTime -> return getBaseURLString() + "reservation/lessonReservation/lessonAvailability?${endpoint}"
                plateTimeAfterLessonTime -> return getBaseURLString() + "reservation/lessonReservation/plateAvailability?${endpoint}"

                coachSignIn -> return getBaseURLString() + "appEmployee/auth/signIn"
                coachMain -> return getBaseURLString() + "appEmployee?${endpoint}"
                coachReservation -> return getBaseURLString() + "appEmployee/reservation/${endpoint}/show"
                customerInfo -> return getBaseURLString() + "appEmployee/user/${endpoint}/show"

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
                plateTimeAfterLessonTime -> return "GET"

                clientSignOut,
                nickNameUpdate,
                pwUpdate -> return "PUT"

                cancelPlateReservation,
                cancelLessonReservation -> return "DELETE"

                coachSignIn -> return "POST"
                coachMain,

                coachReservation,
                customerInfo -> return "GET"

            }
        }

    override var tokenURL: String = baseURL + "auth/common/token"
    override var environmentMode: RequestEnvironmentMode = RequestEnvironmentMode.production
}

