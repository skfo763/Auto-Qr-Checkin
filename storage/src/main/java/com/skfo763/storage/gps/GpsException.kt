package com.skfo763.storage.gps

sealed class GpsException(message: String?): Throwable(message) {

    companion object {
        private const val PERMISSION_DENIED_MESSAGE = "location permission denied"

    }

    class PermissionDeniedException: GpsException(PERMISSION_DENIED_MESSAGE)

}