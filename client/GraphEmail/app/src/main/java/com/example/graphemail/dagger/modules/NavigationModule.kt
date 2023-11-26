package com.example.graphemail.dagger.modules

import com.example.common.router.FeatureEntry
import com.example.core.dagger.FeatureEntryKey
import com.example.login.presentation.LoginEntryImpl
import com.example.login.router.LoginEntry
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface NavigationModule {
    @Binds
    @IntoMap
    @FeatureEntryKey(LoginEntry::class)
    fun loginEntry(entry: LoginEntryImpl): FeatureEntry
}