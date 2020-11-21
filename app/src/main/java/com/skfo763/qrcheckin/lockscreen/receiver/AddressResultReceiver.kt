package com.skfo763.qrcheckin.lockscreen.receiver

import android.app.Activity.RESULT_OK
import android.location.Address
import android.os.Bundle
import android.os.Handler
import android.os.ResultReceiver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.skfo763.base.ServiceResult
import com.skfo763.qrcheckin.lockscreen.service.FetchAddressIntentService
import com.skfo763.qrcheckin.lockscreen.service.FetchAddressIntentService.Companion.FAIL_ILLEGAL_PARAM
import com.skfo763.qrcheckin.lockscreen.service.FetchAddressIntentService.Companion.FAIL_IO_EXCEPTION
import com.skfo763.qrcheckin.lockscreen.service.FetchAddressIntentService.Companion.FAIL_NO_ADDRESS_FOUND
import com.skfo763.qrcheckin.lockscreen.service.FetchAddressIntentService.Companion.RESULT_DATA_KEY
import com.skfo763.qrcheckin.lockscreen.service.FetchAddressIntentService.Companion.SUCCESS_CODE
import com.skfo763.repository.model.CheckInAddress
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class AddressResultReceiver(handler: Handler): ResultReceiver(handler) {

    interface Listener {
        fun onReceiveSuccess(result: CheckInAddress)

        fun onReceiveError(data: Pair<Int, String>)
    }

    var listener: Listener? = null

    override fun onReceiveResult(resultCode: Int, resultData: Bundle?) {
        if(resultCode != RESULT_OK) return
        val output = resultData?.getParcelable<ServiceResult<FetchAddressIntentService.FetchAddress>>(RESULT_DATA_KEY) ?: return
        when(output.code) {
            SUCCESS_CODE -> {
                output.data?.checkInAddress?.let {
                    listener?.onReceiveSuccess(it)
                }
            }
            else -> listener?.onReceiveError(output.code to output.message)
        }
    }

    private val FetchAddressIntentService.FetchAddress.checkInAddress: CheckInAddress
        get() = CheckInAddress(country, largeSiDo, siGunGu, yupMyunDong)
}