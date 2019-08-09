package com.patel.gomap.dagger.main

import com.patel.gomap.dagger.main.MainModule
import com.patel.gomap.dagger.main.MainViewModelModule
import com.patel.gomap.ui.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuildersModule {
    @ContributesAndroidInjector(modules = [MainViewModelModule::class, MainModule::class])
    abstract fun contributesMainActivity(): MainActivity

}