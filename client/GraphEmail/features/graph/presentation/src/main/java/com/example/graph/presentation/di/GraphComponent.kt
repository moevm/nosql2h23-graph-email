package com.example.graph.presentation.di

import com.example.core.dagger.FeatureScoped
import com.example.core.dependency.Dependencies
import com.example.graph.domain.GraphInteractor
import com.example.graph.presentation.ui.GraphViewModel
import dagger.Component

@FeatureScoped
@Component(
    dependencies = [GraphDeps::class]
)
interface GraphComponent {
    @Component.Builder
    interface Builder {
        fun graphDeps(deps: GraphDeps): Builder
        fun build(): GraphComponent
    }
    val graphViewModel: GraphViewModel
}

interface GraphDeps : Dependencies {
    val graphInteractor: GraphInteractor
}
