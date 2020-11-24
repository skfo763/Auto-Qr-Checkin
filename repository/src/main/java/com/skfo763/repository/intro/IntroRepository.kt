package com.skfo763.repository.intro

interface IntroRepository {

    suspend fun saveInitializeState(doInitialize: Boolean)


}