package com.example.graphemail.dagger.modules

import com.example.backend.api.api.ApiService
import com.example.data.repository.provider.NetworkProvider
import dagger.Module
import dagger.Provides
import javax.inject.Qualifier

@Module
class ApiNetworkModule {

    @Provides
    fun providersNetworkProvider(): NetworkProvider = NetworkProvider()

    @Provides
    fun provideApi(
        provider: NetworkProvider
    ) : ApiService = provider.provideRetrofit(ApiService::class.java)
}
