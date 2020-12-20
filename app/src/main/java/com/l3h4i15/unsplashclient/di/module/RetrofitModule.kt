package com.l3h4i15.unsplashclient.di.module

import com.l3h4i15.unsplashclient.network.main.Api
import com.l3h4i15.unsplashclient.network.main.ApiDataContract
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
class RetrofitModule {

    @Provides
    @Singleton
    fun api(retrofit: Retrofit): Api {
        return retrofit.create(Api::class.java)
    }

    @Provides
    fun retrofit(
        okHttpClient: OkHttpClient,
        moshiConverterFactory: MoshiConverterFactory,
        rxJava3CallAdapterFactory: RxJava3CallAdapterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(ApiDataContract.Main.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(moshiConverterFactory)
            .addCallAdapterFactory(rxJava3CallAdapterFactory)
            .build()
    }

    @Provides
    fun okHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor {
                val original = it.request()
                val url = original.url().newBuilder()
                    .addQueryParameter("client_id", ApiDataContract.Main.API_KEY)
                    .build()
                it.proceed(original.newBuilder().url(url).build())
            }
            .build()
    }

    @Provides
    fun rxJava3CallAdapterFactory(): RxJava3CallAdapterFactory {
        return RxJava3CallAdapterFactory.createWithScheduler(Schedulers.io())
    }

    @Provides
    fun moshiConverterFactory(moshi: Moshi): MoshiConverterFactory {
        return MoshiConverterFactory.create(moshi)
    }

    @Provides
    fun moshi(): Moshi {
        return Moshi.Builder().build()
    }
}