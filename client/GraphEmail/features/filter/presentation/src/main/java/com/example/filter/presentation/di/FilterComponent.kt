package com.example.filter.presentation.di

import com.example.core.dagger.FeatureScoped
import com.example.core.dependency.Dependencies
import com.example.filter.domain.FilterInteractor
import com.example.filter.presentation.ui.FilterViewModel
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
    val filterViewModel: FilterViewModel
}

interface FilterDeps : Dependencies {
    val filterInteractor: FilterInteractor
}
