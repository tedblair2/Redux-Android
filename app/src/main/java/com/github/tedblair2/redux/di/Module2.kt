package com.github.tedblair2.redux.di

import com.github.tedblair2.redux.service.AppStore
import com.github.tedblair2.redux.service.CountryService
import com.github.tedblair2.redux.service.CountryServiceImpl
import com.github.tedblair2.redux.service.Store
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class Module2 {
    @Binds
    @Singleton
    abstract fun bindStore(appStore: AppStore):Store

    @Binds
    @Singleton
    abstract fun bindCountryService(countryServiceImpl: CountryServiceImpl):CountryService
}