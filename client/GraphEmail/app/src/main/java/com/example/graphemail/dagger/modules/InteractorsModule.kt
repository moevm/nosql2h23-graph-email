package com.example.graphemail.dagger.modules

import com.example.domain.LoginInteractorImpl
import com.example.login.domain.LoginInteractor
import com.example.login.domain.LoginRepository
import dagger.Module
import dagger.Provides

@Module
class InteractorsModule {
    @Provides
    fun provideLoginInteractor(
        loginRepository: LoginRepository
    ) : LoginInteractor = LoginInteractorImpl(
        loginRepository
    );
}
