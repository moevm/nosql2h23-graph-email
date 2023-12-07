package com.example.filter.router

import com.example.common.router.ComposableFeatureEntry

abstract class FilterEntry : ComposableFeatureEntry {

    final override val featureRoute = "filter_screen"

    fun destination(): String = featureRoute

}