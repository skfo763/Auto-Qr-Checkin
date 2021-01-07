package com.skfo763.qrcheckin.lockscreen.service

import android.app.Activity.RESULT_OK
import android.app.IntentService
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.os.Parcelable
import android.os.ResultReceiver
import com.skfo763.base.model.ServiceResult
import com.skfo763.qrcheckin.R
import kotlinx.parcelize.Parcelize
import java.io.IOException
import java.util.*

const val CLASS_NAME = "FetchAddressService"

class FetchAddressIntentService : IntentService(CLASS_NAME) {

    companion object {
        const val SUCCESS_CODE = 200
        const val FAIL_IO_EXCEPTION = 400
        const val FAIL_ILLEGAL_PARAM = 401
        const val FAIL_NO_ADDRESS_FOUND = 404
        const val RESULT_DATA_KEY = "address_result"

        const val RECEIVER = "receiver"
        const val LATITUDE = "latitude"
        const val LONGITUDE = "longitude"
    }

    @Parcelize
    data class FetchAddress(
        val country: String,
        val largeSiDo: String,
        val siGunGu: String,
        val yupMyunDong: String
    ): Parcelable

    private var receiver: ResultReceiver? = null

    override fun onHandleIntent(intent: Intent?) {
        intent ?: return
        receiver = intent.getParcelableExtra(RECEIVER) ?: return
        val latitude = intent.getDoubleExtra(LATITUDE, 0.0)
        val longitude = intent.getDoubleExtra(LONGITUDE, 0.0)
        val geoCoder = Geocoder(this, Locale.KOREA)
        var addressList = listOf<Address>()

        try {
            addressList = geoCoder.getFromLocation(latitude, longitude, 1)
        } catch (e: IOException) {
            deliverErrorToReceiver(FAIL_IO_EXCEPTION, getString(R.string.unavailable_geocoding))
        } catch (e: IllegalArgumentException) {
            deliverErrorToReceiver(FAIL_ILLEGAL_PARAM, getString(R.string.illegal_parameter))
        }

        if(addressList.isNotEmpty() && addressList[0].getAddressLine(0) != null) {
            deliverResultToReceiver(addressList[0].getAddressLine(0))
        } else {
            deliverErrorToReceiver(FAIL_NO_ADDRESS_FOUND, getString(R.string.no_address_found))
        }
    }

    private fun deliverResultToReceiver(addressLine: String) {
        val addrArray = addressLine.split(" ")
        val fetchAddress = FetchAddress(
            addrArray.getOrNull(0) ?: "",
            addrArray.getOrNull(1) ?: "",
            addrArray.getOrNull(2) ?: "",
            addrArray.getOrNull(3) ?: ""
        )
        val bundle = Bundle().apply {
            putParcelable(RESULT_DATA_KEY, ServiceResult(SUCCESS_CODE, "success", fetchAddress))
        }
        receiver?.send(RESULT_OK, bundle)
    }

    private fun deliverErrorToReceiver(code: Int, message: String) {
        val bundle = Bundle().apply {
            putParcelable(RESULT_DATA_KEY, ServiceResult<FetchAddress>(code, message))
        }
        receiver?.send(RESULT_OK, bundle)
    }
}