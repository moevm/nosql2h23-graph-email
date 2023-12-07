package com.example.graphemail.dagger.deps

import com.example.core.dagger.DependenciesKey
import com.example.core.dependency.Dependencies
import com.example.filter.presentation.di.FilterDeps
import com.example.graphemail.dagger.component.AppComponent
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface FilterDepsModule {
    @Binds
    @IntoMap
    @DependenciesKey(FilterDeps::class)
    fun bindFilerDeps(impl : AppComponent) : Dependencies
}