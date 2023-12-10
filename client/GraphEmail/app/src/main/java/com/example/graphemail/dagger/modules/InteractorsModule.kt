package com.example.graphemail.dagger.modules

import com.example.domain.LoginInteractorImpl
import com.example.domain.MailListInteractorImpl
import com.example.login.domain.LoginInteractor
import com.example.login.domain.LoginRepository
import com.example.mail_list.domain.MailListInteractor
import com.example.mail_list.domain.MailListRepository
import dagger.Module
import dagger.Provides

@Module
class InteractorsModule {
    @Provides
    fun provideLoginInteractor(
        loginRepository: LoginRepository
    ) : LoginInteractor = LoginInteractorImpl(
        loginRepository
    )
    @Provides
    fun provideMailListInteractor(
        mailListRepository: MailListRepository
    ) : MailListInteractor = MailListInteractorImpl(
        mailListRepository
    )
}
