package com.example.mail_list.router

import com.example.common.router.ComposableFeatureEntry

abstract class MailListEntry : ComposableFeatureEntry {

    final override val featureRoute = "mail_list_screen"

    fun destination(): String = featureRoute

}