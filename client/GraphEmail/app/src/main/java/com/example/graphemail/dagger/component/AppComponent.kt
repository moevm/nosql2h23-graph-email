package com.example.graphemail.dagger.component

import android.app.Application
import android.content.Context
import com.example.graphemail.app.GraphEmailApp
import com.example.graphemail.dagger.modules.NavigationModule
import com.example.graphemail.dagger.deps.FeatureDepsModule
import com.example.graphemail.dagger.modules.DataModule
import com.example.graphemail.dagger.modules.InteractorsModule
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides
import javax.inject.Scope
import javax.inject.Singleton

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class AppScope

@Component(
    modules = [
        AppModule::class,
        DataModule::class,
        InteractorsModule::class,
        FeatureDepsModule::class,
        NavigationModule::class
    ]
)
@AppScope
@Singleton
interface AppComponent : FeaturesScreenDeps {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder
        fun build(): AppComponent
    }

    fun inject(application: GraphEmailApp)
}

@Module
class AppModule {
    @AppScope
    @Provides
    fun provideContext(
        application: Application
    ): Context = application.applicationContext
}