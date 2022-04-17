package com.example.application

import android.icu.text.SimpleDateFormat
import android.os.Build
import android.text.TextUtils
import androidx.annotation.RequiresApi
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import okhttp3.logging.HttpLoggingInterceptor

const val serverAddress ="http://121.188.98.211:1350"
object ServiceGenerator {

    private const val BASE_URL =serverAddress
    private val httpClient: OkHttpClient.Builder = OkHttpClient.Builder()
    private val builder: Retrofit.Builder = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
    private var retrofit = builder.build()
    fun <S> createService(serviceClass: Class<S>?): S {
        return createService(serviceClass, null)
    }
    fun <S> createService(
        serviceClass: Class<S>?, authToken: String?
    ): S {
        if (!TextUtils.isEmpty(authToken)) {
            val interceptor = AuthenticationInterceptor("Bearer $authToken")

            if (!httpClient.interceptors().contains(interceptor)) {
                httpClient.addInterceptor(interceptor)
                builder.client(httpClient.build())
                retrofit = builder.build()
            }
        }
        return retrofit.create(serviceClass)
    }
}

class AuthenticationInterceptor(private val authToken: String) : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {

        val original: Request = chain.request()
        val builder = original.newBuilder()
            .header("Authorization", authToken)
        val request = builder.build()
        return chain.proceed(request)
    }
}


