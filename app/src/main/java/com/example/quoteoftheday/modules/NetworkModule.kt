package com.example.quoteoftheday.modules

import android.content.Context
import com.example.quoteoftheday.CACHE_FILE_NAME
import com.example.quoteoftheday.network.QuotesApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.Cache
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.io.File
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideRetrofit(@ApplicationContext context: Context) : Retrofit {
        val size = (2 * 1024 * 1024).toLong() // 2MB
        val cache = Cache(
            File(context.cacheDir, CACHE_FILE_NAME),
            size
        )

        val okHttpClient = OkHttpClient.Builder()
            .cache(cache)
            .addInterceptor { chain ->
                var request = chain.request()
                // TODO: Customize Cache-Control
                // refer to https://bapspatil.medium.com/caching-with-retrofit-store-responses-offline-71439ed32fda
                request = request.newBuilder().header(
                    "Cache-Control",
                    "max-age=" + 60 * 60 * 24
                )
                    .build()
                chain.proceed(request)
            }
            .build()

        return Retrofit.Builder()
            .baseUrl("https://zenquotes.io/api")
            .addConverterFactory(
                Json.asConverterFactory("application/json".toMediaTypeOrNull()!!)
            )
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit) : QuotesApiService {
        return retrofit.create(QuotesApiService::class.java)
    }

}