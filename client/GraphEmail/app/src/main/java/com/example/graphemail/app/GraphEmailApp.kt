package com.example.graphemail.app

import android.app.Application
import com.example.common.router.Destinations
import com.example.core.dependency.DepsMap
import com.example.core.dependency.HasDependencies
import com.example.graphemail.dagger.component.DaggerAppComponent
import javax.inject.Inject

class GraphEmailApp : Application(), HasDependencies {

    @Inject
    override lateinit var depsMap: DepsMap

    @Inject
    override lateinit var depsNav: Destinations

    override fun onCreate() {
        super.onCreate()

        DaggerAppComponent.builder()
            .application(this)
            .build()
            .inject(this)
    }
}