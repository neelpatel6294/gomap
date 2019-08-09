package com.patel.gomap.dagger.app

import android.app.Application
import com.patel.gomap.BaseApplication
import com.patel.gomap.dagger.main.ActivityBuildersModule
import com.patel.gomap.dagger.viewmodel.ViewModelFactoryModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [AndroidSupportInjectionModule::class,
    ViewModelFactoryModule::class,
    ActivityBuildersModule::class,
    AppModule::class])
interface AppComponent : AndroidInjector<BaseApplication> {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder
        fun build(): AppComponent
    }
}