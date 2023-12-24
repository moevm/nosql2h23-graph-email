package com.example.graphemail.dagger.deps

import com.example.core.dagger.DependenciesKey
import com.example.core.dependency.Dependencies
import com.example.graph.presentation.di.GraphDeps
import com.example.graphemail.dagger.component.AppComponent
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface GraphDepsModule {
    @Binds
    @IntoMap
    @DependenciesKey(GraphDeps::class)
    fun bindGraphDeps(impl : AppComponent) : Dependencies
}