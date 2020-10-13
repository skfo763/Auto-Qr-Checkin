
package com.skfo763.storage.datastore

import android.content.Context
import android.util.Log
import androidx.datastore.DataStore
import androidx.datastore.preferences.Preferences
import androidx.datastore.preferences.createDataStore
import androidx.datastore.preferences.edit
import androidx.datastore.preferences.preferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LockScreenDataStore (context: Context) {

    private val IS_FEATURE_ON = preferencesKey<Boolean>("is_feature_on")

    private val lockScreenFeatureStore: DataStore<Preferences> = context.createDataStore(name = "lock_screen_feature")
    private val widgetFeatureStore: DataStore<Preferences> = context.createDataStore(name = "show_widget_feature")

    val lockScreenStateFlow: Flow<Boolean> = lockScreenFeatureStore.data.map {
        it[IS_FEATURE_ON] ?: false
    }

    val widgetFeatureStateFlow: Flow<Boolean> = widgetFeatureStore.data.map {
        it[IS_FEATURE_ON] ?: false
    }

    suspend fun setLockScreenFeatureState(isFeatureOn: Boolean) {
        lockScreenFeatureStore.edit {
            it[IS_FEATURE_ON] = isFeatureOn
        }
    }

    suspend fun setWidgetFeatureState(isFeatureOn: Boolean) {
        widgetFeatureStore.edit {
            it[IS_FEATURE_ON] = isFeatureOn
        }
    }
}
