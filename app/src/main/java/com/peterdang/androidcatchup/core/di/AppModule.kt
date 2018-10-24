package com.peterdang.androidcatchup.core.di

import android.annotation.SuppressLint
import android.content.Context
import com.peterdang.androidcatchup.BuildConfig
import com.peterdang.androidcatchup.MyApplication
import com.peterdang.androidcatchup.R
import com.peterdang.androidcatchup.core.utils.ConfigReader
import com.peterdang.androidcatchup.core.utils.ImageUtils
import com.peterdang.androidcatchup.features.location.helper.LocationRequestHelper
import com.peterdang.androidcatchup.features.location.helper.LocationResultHelper
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager
import javax.security.cert.CertificateException

@Module
class AppModule(private val myApp: MyApplication) {
    @Provides
    @Singleton
    fun provideApplicationContext(): Context = myApp

    @Provides
    @Singleton
    fun provideConfigReader(): ConfigReader = ConfigReader(myApp)

    @Provides
    @Singleton
    fun provideLocationResultUtil(): LocationResultHelper = LocationResultHelper(myApp)

    @Provides
    @Singleton
    fun provideLocationRequestHelper(): LocationRequestHelper = LocationRequestHelper(myApp)

    @Provides
    @Singleton
    fun provideImageUtils(): ImageUtils = ImageUtils(myApp)

    @Provides
    @Singleton
    fun provideRetrofit(configReader: ConfigReader): Retrofit {
        val baseUrl = configReader.getConfigValue(myApp.getString(R.string.config_base_url))

        return Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(createClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
    }

    private fun createClient(): OkHttpClient {
        val okHttpClientBuilder: OkHttpClient.Builder = OkHttpClient.Builder()
        if (BuildConfig.DEBUG) {
            val loggingInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC)
            okHttpClientBuilder.addInterceptor(loggingInterceptor)
        }
        val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
            @SuppressLint("TrustAllX509TrustManager")
            @Throws(CertificateException::class)
            override fun checkClientTrusted(chain: Array<java.security.cert.X509Certificate>, authType: String) {
            }

            @SuppressLint("TrustAllX509TrustManager")
            @Throws(CertificateException::class)
            override fun checkServerTrusted(chain: Array<java.security.cert.X509Certificate>, authType: String) {
            }

            override fun getAcceptedIssuers(): Array<java.security.cert.X509Certificate> {
                return arrayOf()
            }
        })

        // Install the all-trusting trust manager
        val sslContext = SSLContext.getInstance("SSL")
        sslContext.init(null, trustAllCerts, java.security.SecureRandom())
        // Create an ssl socket factory with our all-trusting manager
        val sslSocketFactory = sslContext.socketFactory

        val builder = OkHttpClient.Builder()
        builder.sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
        builder.hostnameVerifier { _, _ -> true }
        return okHttpClientBuilder.build()
    }

}