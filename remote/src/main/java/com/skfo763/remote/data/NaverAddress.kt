package com.skfo763.remote.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NaverAddress(
    // 변환 타입
    @Json(name = "name") val convertType: String,

    // 주소 정보
    @Json(name = "region") val regions: ComplexRegion
)

@JsonClass(generateAdapter = true)
data class ComplexRegion(
    // 국가
    @Json(name = "area0") val countryRegion: Region,

    // 시/도
    @Json(name = "area1") val largeSiDoRegion: Region,

    // 시/군/구
    @Json(name = "area2") val siGunGuRegion: Region,

    // 읍/면/동
    @Json(name = "area3") val yupMyunDongRegion: Region
    
)

@JsonClass(generateAdapter = true)
data class Region(
    @Json(name = "name") val name: String,
    @Json(name = "coords") val coordinates: ComplexCoords
)

@JsonClass(generateAdapter = true)
data class ComplexCoords(
    @Json(name = "center") val center: Coordinate
)

@JsonClass(generateAdapter = true)
data class Coordinate(
    @Json(name = "crs") val coordinateType: String,
    @Json(name = "x") val x: Float,
    @Json(name = "y") val y: Float
)