package com.github.tedblair2.redux.di

import com.apollographql.apollo3.ApolloClient
import com.github.tedblair2.redux.service.CountryService
import com.github.tedblair2.redux.service.CountryServiceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Module {

    @Provides
    @Singleton
    fun provideApolloClient(): ApolloClient {
        return ApolloClient.Builder()
            .serverUrl("https://countries.trevorblades.com/graphql")
            .build()
    }

    @Provides
    @Singleton
    fun provideCountryService(apolloClient: ApolloClient): CountryService {
        return CountryServiceImpl(apolloClient)
    }
}