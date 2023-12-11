package com.example.graphemail.dagger.modules

import com.example.backend.api.api.ApiService
import com.example.data.repository.repository.GraphRepositoryImpl
import com.example.data.repository.repository.LoginRepositoryImpl
import com.example.data.repository.repository.MailListRepositoryImpl
import com.example.graph.domain.GraphRepository
import com.example.login.domain.LoginRepository
import com.example.mail_list.domain.MailListRepository
import dagger.Module
import dagger.Provides
import javax.inject.Qualifier

@Module(includes = [ApiNetworkModule::class])
class DataModule {
    @Provides
    fun provideLoginRepository(
        apiService: ApiService
    ) : LoginRepository = LoginRepositoryImpl(
        apiService
    )

    @Provides
    fun provideMailListRepository(
        apiService: ApiService
    ) : MailListRepository = MailListRepositoryImpl(
        apiService
    )

    @Provides
    fun provideGraphRepository(
        apiService: ApiService
    ) : GraphRepository = GraphRepositoryImpl(
        apiService
    )
}
