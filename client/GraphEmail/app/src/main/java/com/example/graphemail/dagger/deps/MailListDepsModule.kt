package com.example.graphemail.dagger.deps

import com.example.core.dagger.DependenciesKey
import com.example.core.dependency.Dependencies
import com.example.graphemail.dagger.component.AppComponent
import com.example.mail_list.presentation.di.MailListDeps
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface MailListDepsModule {
    @Binds
    @IntoMap
    @DependenciesKey(MailListDeps::class)
    fun bindMailListDeps(impl : AppComponent) : Dependencies
}