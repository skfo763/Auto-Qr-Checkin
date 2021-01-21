package com.skfo763.storage.datastore

import android.content.Context
import androidx.datastore.DataStore
import androidx.datastore.preferences.Preferences
import androidx.datastore.preferences.createDataStore
import androidx.datastore.preferences.edit
import androidx.datastore.preferences.preferencesKey
import com.skfo763.base.theme.ThemeType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AppDataStore(context: Context) {

    private val LANGUAGE = preferencesKey<String>("language")
    private val IS_FEATURE_ON = preferencesKey<Boolean>("is_feature_on")
    private val INIT_SETTING = preferencesKey<Boolean>("should_init_setting")
    private val DO_AUTO_CHECKIN = preferencesKey<Boolean>("do_auto_checkin")
    private val APP_ICON_TYPE = preferencesKey<String>("app_icon_type")
    private val QR_CHECKIN_TYPE = preferencesKey<String>("qr_checkin_type")
    private val UI_THEME = preferencesKey<String>("ui_theme")

    private val deleteAdsState: DataStore<Preferences> = context.createDataStore(name = "delete_ads_state")
    private val language: DataStore<Preferences> = context.createDataStore(name = "language")
    private val initSettingState: DataStore<Preferences> = context.createDataStore(name = "should_init_setting")
    private val autoCheckInState: DataStore<Preferences> = context.createDataStore(name = "auto_checkin_state")
    private val appIconType: DataStore<Preferences> = context.createDataStore(name = "app_icon_type")
    private val qrCheckinType: DataStore<Preferences> = context.createDataStore(name = "qr_checkin_type")
    private val currentUiTheme: DataStore<Preferences> = context.createDataStore(name = "current_ui_theme")

    val deleteAdsStateFlow: Flow<Boolean> = deleteAdsState.data.map {
        it[IS_FEATURE_ON] ?: false
    }

    val languageFlow: Flow<String> = language.data.map {
        it[LANGUAGE] ?: "korean"
    }

    val initSettingStateFlow: Flow<Boolean> = initSettingState.data.map { it[INIT_SETTING] ?: true }

    val autoCheckInStateFlow: Flow<Boolean> = autoCheckInState.data.map { it[DO_AUTO_CHECKIN] ?: false }

    val appIconTypeFlow: Flow<String> = appIconType.data.map { it[APP_ICON_TYPE] ?: "com.skfo763.qrcheckin.launch.LightIconLauncher" }

    val qrCheckinTypeFlow: Flow<String> = qrCheckinType.data.map { it[QR_CHECKIN_TYPE] ?: "naver" }

    val uiThemeTypeFlow: Flow<ThemeType> = currentUiTheme.data.map {
        val themeTypeString = it[UI_THEME] ?: ThemeType.DEFAULT_MODE.name
        return@map ThemeType.valueOf(themeTypeString)
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

    suspend fun setInitSettingState(initSettingState: Boolean) {
        this.initSettingState.edit {
            it[INIT_SETTING] = initSettingState
        }
    }

    suspend fun setDoAutoCheckInState(autoCheckInState: Boolean) {
        this.autoCheckInState.edit {
            it[DO_AUTO_CHECKIN] = autoCheckInState
        }
    }

    suspend fun setAppIconType(type: String) {
        this.appIconType.edit {
            it[APP_ICON_TYPE] = type
        }
    }

    suspend fun setQrCheckinType(type: String) {
        this.qrCheckinType.edit {
            it[QR_CHECKIN_TYPE] = type
        }
    }

    suspend fun setCurrentUiTheme(type: ThemeType) {
        this.currentUiTheme.edit {
            it[UI_THEME] = type.name
        }
    }

}