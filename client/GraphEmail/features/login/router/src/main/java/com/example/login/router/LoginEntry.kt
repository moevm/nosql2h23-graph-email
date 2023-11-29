package com.example.login.router

import com.example.common.router.ComposableFeatureEntry

abstract class LoginEntry : ComposableFeatureEntry {

    final override val featureRoute = "login_screen"

    fun destination(): String = featureRoute

}