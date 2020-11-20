package com.skfo763.remote.api

import com.skfo763.remote.base.NaverBaseResult
import com.skfo763.remote.data.NaverAddress
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface NaverMapApi {

    companion object {
        const val REVERSE_GEOCODE_ENDPOINT = "/map-reversegeocode/v2"
    }

    @GET("$REVERSE_GEOCODE_ENDPOINT/gc")
    suspend fun getAddressByLocation(
        @Query("coords", encoded = true) coordinate: String,
        @Query("sourcecrs", encoded = true) inputCrs: String? = null,
        @Query("targetcrs", encoded = true) targetCrs: String? = null,
        @Query("orders", encoded = true) orders: String? = null,
        @Query("output") outputType: String = "json"
    ): NaverBaseResult<NaverAddress>


}