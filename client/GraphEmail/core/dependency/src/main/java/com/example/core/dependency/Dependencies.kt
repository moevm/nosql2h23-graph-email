package com.example.core.dependency

import com.example.common.router.Destinations

interface Dependencies

typealias DepsMap = Map<Class<out Dependencies>, @JvmSuppressWildcards Dependencies>

interface HasDependencies {
    val depsMap: DepsMap
    val depsNav: Destinations
}