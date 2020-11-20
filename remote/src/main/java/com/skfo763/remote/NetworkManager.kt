package com.skfo763.remote

import com.facebook.stetho.okhttp3.StethoInterceptor
import okhttp3.*
import okhttp3.internal.EMPTY_RESPONSE
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.lang.reflect.Type
import java.util.concurrent.TimeUnit

class NetworkManager {

    fun getNaverApiRetrofit(): Retrofit = Retrofit.Builder().baseUrl(BuildConfig.NAVER_MAP_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .client(OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .addNetworkInterceptor(NaverHeaderInterceptor())
                .addNetworkInterceptor(StethoInterceptor())
                .apply {
                    if(BuildConfig.DEBUG) {
                        addInterceptor(HttpLoggingInterceptor().apply {
                            this.level = HttpLoggingInterceptor.Level.BODY
                        })
                    }
                }.build())
            .build()

    class NaverHeaderInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val interceptedRequest = chain.request().newBuilder()
                .header("X-NCP-APIGW-API-KEY-ID", BuildConfig.NAVER_MAP_CLIENT_ID)
                .header("X-NCP-APIGW-API-KEY", BuildConfig.NAVER_MAP_CLIENT_SECRET)
                .build()
            return chain.proceed(interceptedRequest)
        }
    }

    class NullOnEmptyConverterFactory : Converter.Factory() {
        override fun responseBodyConverter(
            type: Type,
            annotations: Array<out Annotation>,
            retrofit: Retrofit
        ): Converter<ResponseBody, *>? {
            val delegate = retrofit.nextResponseBodyConverter<Any>(this, type, annotations)
            return Converter<ResponseBody, Any> {
                if (it.contentLength() == 0L) return@Converter EMPTY_RESPONSE
                delegate.convert(it)
            }
        }
    }
}