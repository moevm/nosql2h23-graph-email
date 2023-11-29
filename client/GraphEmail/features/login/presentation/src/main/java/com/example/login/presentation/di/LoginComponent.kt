package com.example.login.presentation.di

import com.example.core.dagger.FeatureScoped
import com.example.core.dependency.Dependencies
import com.example.login.domain.LoginInteractor
import com.example.login.presentation.ui.LoginViewModel
import dagger.Component

@FeatureScoped
@Component(
    dependencies = [LoginDeps::class]
)
interface LoginComponent {
    @Component.Builder
    interface Builder {
        fun loginDeps(deps: LoginDeps): Builder
        fun build(): LoginComponent
    }
    val loginViewModel: LoginViewModel
}

interface LoginDeps : Dependencies {
    val loginInteractor: LoginInteractor
}
