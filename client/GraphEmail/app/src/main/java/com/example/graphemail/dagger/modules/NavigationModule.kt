package com.example.graphemail.dagger.modules

import com.example.common.router.FeatureEntry
import com.example.core.dagger.FeatureEntryKey
import com.example.filter.presentation.FilterEntryImpl
import com.example.filter.router.FilterEntry
import com.example.graph.presentation.GraphEntryImpl
import com.example.login.presentation.LoginEntryImpl
import com.example.login.router.LoginEntry
import com.example.mail_list.presentation.MailListEntryImpl
import com.example.mail_list.router.MailListEntry
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import com.example.graph.router.GraphEntry as GraphEntry

@Module
interface NavigationModule {
    @Binds
    @IntoMap
    @FeatureEntryKey(LoginEntry::class)
    fun loginEntry(entry: LoginEntryImpl): FeatureEntry

    @Binds
    @IntoMap
    @FeatureEntryKey(MailListEntry::class)
    fun mailListEntry(entry: MailListEntryImpl): FeatureEntry

    @Binds
    @IntoMap
    @FeatureEntryKey(FilterEntry::class)
    fun filterEntry(entry: FilterEntryImpl): FeatureEntry

    @Binds
    @IntoMap
    @FeatureEntryKey(GraphEntry::class)
    fun graphEntry(entry: GraphEntryImpl): FeatureEntry
}