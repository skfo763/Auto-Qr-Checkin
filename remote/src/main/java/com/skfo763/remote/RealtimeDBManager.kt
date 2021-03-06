package com.skfo763.remote

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.skfo763.remote.data.IntroYoutube
import com.skfo763.remote.data.KakaoQrApiUrl
import com.skfo763.remote.data.NaverQrApiUrl
import com.skfo763.remote.data.QrCheckInError
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.flow.callbackFlow

class RealtimeDBManager {

    private val database = Firebase.database

    @ExperimentalCoroutinesApi
    val naverQrApiUrl  = callbackFlow<NaverQrApiUrl> {
        val dbRef = database.getReference("availableServer").child("naver")
        val listener = dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val url = snapshot.child("url").getValue(String::class.java) ?: BuildConfig.QR_CHECKIN_URL_NAVER
                val availableHosts = snapshot.child("host").children.toCustomDataTypeList(String::class.java, "")
                val availablePaths = snapshot.child("path").children.toCustomDataTypeList(String::class.java, "")
                val appLandingScheme = snapshot.child("landing").children.toCustomDataTypeList(String::class.java, "")
                val errorList = snapshot.child("error").children.toCustomDataTypeList(QrCheckInError::class.java, QrCheckInError())
                this@callbackFlow.sendBlocking(NaverQrApiUrl(url, availableHosts, availablePaths, appLandingScheme, errorList))
            }

            override fun onCancelled(error: DatabaseError) {
                this@callbackFlow.close(error.toException())
            }
        })

        awaitClose {
            dbRef.removeEventListener(listener)
        }
    }

    @ExperimentalCoroutinesApi
    val kakaoQrApiUrl = callbackFlow<KakaoQrApiUrl> {
        val dbRef = database.getReference("availableServer").child("kakao")
        val listener = dbRef.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val url = snapshot.child("url").getValue(String::class.java)
                val browseUrl = snapshot.child("params").child("url").getValue(String::class.java)
                this@callbackFlow.sendBlocking(KakaoQrApiUrl(url, browseUrl))
            }
            override fun onCancelled(error: DatabaseError) {
                this@callbackFlow.close(error.toException())
            }
        })

        awaitClose {
            dbRef.removeEventListener(listener)
        }
    }

    @ExperimentalCoroutinesApi
    val playStoreUrl = callbackFlow<String> {
        val dbRef = database.getReference("playStore").child("url")
        val listener = dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val url = snapshot.getValue(String::class.java) ?: BuildConfig.QR_CHECKIN_URL_NAVER
                this@callbackFlow.sendBlocking(url)
            }

            override fun onCancelled(error: DatabaseError) {
                this@callbackFlow.close(error.toException())
            }
        })

        awaitClose {
            dbRef.removeEventListener(listener)
        }
    }

    @ExperimentalCoroutinesApi
    val introVideoInfo = callbackFlow<IntroYoutube> {
        val dbRef = database.getReference("intro").child("youtube_video")
        val listener = dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val id = snapshot.child("id").getValue(String::class.java)
                val checkPointList = snapshot.child("checkpoints").children.toCustomDataTypeList(Int::class.java, 0)
                this@callbackFlow.sendBlocking(IntroYoutube(id, checkPointList))
            }

            override fun onCancelled(error: DatabaseError) {
                this@callbackFlow.close(error.toException())
            }
        })

        awaitClose {
            dbRef.removeEventListener(listener)
        }
    }

    private fun <T> Iterable<DataSnapshot>.toCustomDataTypeList(clazz: Class<T>, dataOnNull: T): List<T> {
        return this.toList().map { it.getValue(clazz) ?: dataOnNull }
    }

}