package com.patel.gomap.dagger.app

import android.app.Application
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton
import androidx.room.Room
import com.patel.gomap.room.PinDAO
import com.patel.gomap.room.PinDatabase
import com.patel.gomap.room.Repository

@Module
object AppModule {

    @Singleton
    @JvmStatic
    @Provides
    fun providePinDatabase(application: Application): PinDatabase {
        return Room.databaseBuilder(application, PinDatabase::class.java, "database").build()
    }

    @Singleton
    @JvmStatic
    @Provides
    fun providePinDao(pinDatabase: PinDatabase): PinDAO {
        return pinDatabase.pinDAO()
    }

    @Singleton
    @JvmStatic
    @Provides
    fun provideRepository(pinDAO: PinDAO): Repository {
        return Repository(pinDAO)
    }
    @Singleton
    @JvmStatic
    @Provides
    fun providesGson() : Gson = GsonBuilder().create()

    @Singleton
    @JvmStatic
    @Provides
    fun provideRetrofitInstance(gson : Gson, client : OkHttpClient) : Retrofit
    {
        return Retrofit.Builder()
            .baseUrl("https://annetog.gotenna.com")
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build()
    }

    @Singleton
    @JvmStatic
    @Provides
    fun providesHTTPClient() :OkHttpClient
    {
        val builder = OkHttpClient.Builder()
        return builder.build()
    }
}