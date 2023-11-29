package com.example.core.dagger

import com.example.common.router.FeatureEntry
import com.example.core.dependency.Dependencies
import dagger.MapKey
import javax.inject.Scope
import kotlin.reflect.KClass

@MapKey
annotation class DependenciesKey(val value: KClass<out Dependencies>)

@MapKey
annotation class FeatureEntryKey(val value: KClass<out FeatureEntry>)


@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class FeatureScoped

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class SubfeatureScoped