package com.skfo763.repository.checkinmap

sealed class CheckInMapException(message: String?) : Exception(message) {

    class NoAddressInfoException(message: String? = null): CheckInMapException(message)

    class NoCheckPointException(message: String?) : CheckInMapException(message)

    class CannnotFoundLocationException(message: String?) : CheckInMapException(message)

}