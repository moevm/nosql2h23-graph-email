package com.example.graphemail.dagger.modules

import com.example.backend.api.api.ApiService
import com.example.data.repository.repository.LoginRepositoryImpl
import com.example.login.domain.LoginRepository
import dagger.Module
import dagger.Provides
import javax.inject.Qualifier


@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class AppVersion

@Module(includes = [ApiNetworkModule::class])
class DataModule {
    @Provides
    fun provideLoginRepository(
        apiService: ApiService
    ) : LoginRepository = LoginRepositoryImpl(
        apiService
    )
}
