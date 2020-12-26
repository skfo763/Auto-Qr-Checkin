package com.skfo763.repository.model

enum class CheckInType(val type: String) {
    NAVER("naver"),
    KAKAO("kakao"),
    UNKNOWN("unknown")
}

val String.qrCheckInType: CheckInType get() = when(this) {
    CheckInType.NAVER.type -> CheckInType.NAVER
    CheckInType.KAKAO.type -> CheckInType.KAKAO
    else -> CheckInType.UNKNOWN
}