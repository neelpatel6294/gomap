package com.patel.gomap.dagger.viewmodel

import androidx.lifecycle.ViewModelProvider
import com.patel.gomap.viewModel.ViewModelProviderFactory
import dagger.Binds
import dagger.Module

@Module
abstract class ViewModelFactoryModule {
    @Binds
    internal abstract fun bindViewModelFactory(viewModelsProvidersFactory: ViewModelProviderFactory) : ViewModelProvider.Factory
}