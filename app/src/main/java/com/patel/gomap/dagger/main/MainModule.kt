package com.patel.gomap.dagger.main

import com.patel.gomap.network.GoTennaAPI
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
object MainModule {

    @Provides
    @JvmStatic
    fun provideMainAPI (retrofit : Retrofit): GoTennaAPI
    {
        return retrofit.create(GoTennaAPI::class.java)
    }
}