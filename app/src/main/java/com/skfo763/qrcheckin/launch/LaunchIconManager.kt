package com.skfo763.qrcheckin.launch

import android.app.Activity
import android.content.ComponentName
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.ThemeUtils
import com.skfo763.base.theme.ThemeType
import com.skfo763.base.theme.getTheme
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class LaunchIconManager @Inject constructor(private val activity: Activity) {

    companion object {
        fun getType(manifestName: String, currentUiTheme: ThemeType) = when(manifestName) {
            Type.LIGHT.manifestName -> Type.LIGHT
            Type.DARK.manifestName -> Type.DARK
            else -> if(currentUiTheme == ThemeType.DARK_MODE) Type.DARK else Type.LIGHT
        }
    }

    enum class Type(val manifestName: String) {
        LIGHT("com.skfo763.qrcheckin.launch.LightIconLauncher"),
        DARK("com.skfo763.qrcheckin.launch.DarkIconLauncher")
    }

    private val enableTypes = listOf(Type.LIGHT, Type.DARK)

    fun setIcon(launcherType: Type) {
        if(!enableTypes.contains(launcherType)) return
        activity.packageManager.setComponentEnabledSetting(
            ComponentName(activity.application.packageName, launcherType.manifestName),
            PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP
        )
        enableTypes.filter { type -> type != launcherType }.forEach {
            try {
                activity.packageManager.setComponentEnabledSetting(
                    ComponentName(activity.application.packageName, it.manifestName),
                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}