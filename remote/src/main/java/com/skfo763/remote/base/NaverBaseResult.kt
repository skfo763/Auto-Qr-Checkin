package com.skfo763.remote.base

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NaverBaseResult <T> (

    @Json(name = "status") val status: Status,

    @Json(name = "results") val results: List<T>

)

@JsonClass(generateAdapter = true)
data class Status(

    @Json(name = "code") val code: Int,

    @Json(name = "name") val name: String,

    @Json(name = "message") val message: String

)