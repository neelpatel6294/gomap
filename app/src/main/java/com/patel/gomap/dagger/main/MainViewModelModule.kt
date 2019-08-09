package com.patel.gomap.dagger.main

import androidx.lifecycle.ViewModel
import com.patel.gomap.dagger.viewmodel.ViewModelKey
import com.patel.gomap.ui.MainViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class MainViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun bindMainViewModel(viewModel: MainViewModel) : ViewModel
}