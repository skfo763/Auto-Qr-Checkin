package com.skfo763.qrcheckin.launch

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.skfo763.qrcheckin.intro.activity.IntroActivity
import com.skfo763.qrcheckin.lockscreen.activity.LockScreenActivity
import com.skfo763.storage.datastore.AppDataStore
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@AndroidEntryPoint
class LaunchActivity : AppCompatActivity() {

    @Inject lateinit var appDataStore: AppDataStore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        GlobalScope.launch {
            navigateMainActivity()
        }
    }

    private suspend fun navigateMainActivity() {
        appDataStore.initSettingStateFlow.collect { isInitSetting ->
            moveActivity(isInitSetting)
        }
    }

    private suspend fun moveActivity(isInitSetting: Boolean) {
        withContext(Dispatchers.Main) {
            if(isInitSetting) {
                startActivity(IntroActivity.getIntent(this@LaunchActivity))
            } else {
                startActivity(LockScreenActivity.getIntent(this@LaunchActivity))
            }
            finish()
        }
    }
}