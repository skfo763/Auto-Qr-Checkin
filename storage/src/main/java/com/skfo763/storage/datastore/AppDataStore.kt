package com.skfo763.storage.datastore

import android.content.Context
import androidx.datastore.DataStore
import androidx.datastore.preferences.Preferences
import androidx.datastore.preferences.createDataStore
import androidx.datastore.preferences.edit
import androidx.datastore.preferences.preferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AppDataStore(context: Context) {

    private val LANGUAGE = preferencesKey<String>("language")
    private val IS_FEATURE_ON = preferencesKey<Boolean>("is_feature_on")

    private val deleteAdsState: DataStore<Preferences> = context.createDataStore(name = "delete_ads_state")
    private val language: DataStore<Preferences> = context.createDataStore(name = "language")

    val deleteAdsStateFlow: Flow<Boolean> = deleteAdsState.data.map {
        it[IS_FEATURE_ON] ?: false
    }

    val languageFlow: Flow<String> = language.data.map {
        it[LANGUAGE] ?: "korean"
    }

    suspend fun setDeleteAdsState(isFeatureOn: Boolean) {
        deleteAdsState.edit {
            it[IS_FEATURE_ON] = isFeatureOn
        }
    }

    suspend fun setLanguage(language: String) {
        this.language.edit {
            it[LANGUAGE] = language
        }
    }

}