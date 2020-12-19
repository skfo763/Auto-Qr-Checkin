package com.skfo763.gradle.internalsharing

import com.google.auth.oauth2.GoogleCredentials
import org.gradle.api.DefaultTask
import org.gradle.kotlin.dsl.get
import java.io.FileInputStream

open class InternalSharingTask : DefaultTask() {

    companion object {
        private const val APPLICATON_ID = "com.skfo763.qrcheckin"
        private const val PUBLISH_SCOPE = "https://www.googleapis.com/auth/androidpublisher"
    }

    init {
        group = "skfo763"
        description = "Staging to Google play console internal sharing server"
    }

    fun publish() {
        val ext = project.extensions["internalSharing"] as? InternalSharingPluginExt ?: return
        val token = getToken() ?: return
        val downloadUrl = uploadPlayStoreInternalSharing(token, ext.aabFileLocation) ?: return
    }

    private fun getToken(): String? {
        val credential = GoogleCredentials
            .fromStream(FileInputStream("buildSrc/key.json"))
            .createScoped(PUBLISH_SCOPE)
        credential.refreshIfExpired()
        return credential.accessToken.tokenValue
    }

    private fun uploadPlayStoreInternalSharing(
        token: String,
        aabFileLocation: String?
    ): String? {
        return null
        /*
        val transport = buildTransport()
        val factory = JacksonFactory.getDefaultInstance()
        val publisher = AndroidPublisher.Builder(transport, factory)
        {
            val contentType = HttpHeaders()
            contentType.contentType = "application/octet-stream"
            it.headers = contentType
            it.connectTimeout = 2*60000
            it.readTimeout = 2*60000
        }
            .setApplicationName(applicationName)
            .build()

        return publisher.internalappsharingartifacts()
            .uploadbundle(
                "com.NoonDate",
                FileContent("application/octet-stream", File(aabFileLocation))
            )
            .setOauthToken(token)
            .trackUploadProgress("App Bundle", File(aabFileLocation))
            .execute()
            .downloadUrl
         */
    }

}