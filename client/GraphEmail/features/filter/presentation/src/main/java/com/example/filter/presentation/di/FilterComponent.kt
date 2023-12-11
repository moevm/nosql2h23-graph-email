package com.example.filter.presentation.di

import com.example.core.dagger.FeatureScoped
import com.example.core.dependency.Dependencies
import dagger.Component

@FeatureScoped
@Component(
    dependencies = [FilterDeps::class]
)
interface FilterComponent {
    @Component.Builder
    interface Builder {
        fun filterDeps(deps: FilterDeps): Builder
        fun build(): FilterComponent
    }
}

interface FilterDeps : Dependencies
