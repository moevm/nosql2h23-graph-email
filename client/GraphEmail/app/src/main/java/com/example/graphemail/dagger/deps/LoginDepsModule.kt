package com.example.graphemail.dagger.deps

import com.example.core.dagger.DependenciesKey
import com.example.core.dependency.Dependencies
import com.example.graphemail.dagger.component.AppComponent
import com.example.login.presentation.di.LoginDeps
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface LoginDepsModule {
    @Binds
    @IntoMap
    @DependenciesKey(LoginDeps::class)
    fun bindLoginDeps(impl : AppComponent) : Dependencies
}