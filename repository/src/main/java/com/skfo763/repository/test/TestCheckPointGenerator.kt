package com.skfo763.repository.test

import com.skfo763.repository.model.CheckInAddress
import com.skfo763.repository.model.CheckPoint
import kotlin.random.Random

class TestCheckPointGenerator {

    fun getRandomCheckPoint(): CheckPoint {
        return CheckPoint(
            checkPointIdx = Random.nextInt(0, 100000),
            latitude = Random.nextDouble(37.512999, 37.715133),
            longitude = Random.nextDouble(127.102268, 127.269311),
            address = CheckInAddress(
                "대한민국",
                "서울특별시",
                "뫄뫄구",
                "솨솨동"
            ),
            checkInTime = Random.nextLong(System.currentTimeMillis() - 6000000, System.currentTimeMillis())
        )
    }

}