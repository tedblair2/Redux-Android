package com.github.tedblair2.redux.service

import com.apollographql.apollo3.ApolloClient
import com.github.tedblair2.CountriesQuery
import com.github.tedblair2.CountryQuery
import com.github.tedblair2.redux.mappers.toDetailedCountry
import com.github.tedblair2.redux.mappers.toSimpleCountry
import com.github.tedblair2.redux.model.DetailedCountry
import com.github.tedblair2.redux.model.SimpleCountry
import javax.inject.Inject

class CountryServiceImpl @Inject constructor(
    private val apolloClient: ApolloClient
) : CountryService {

    override suspend fun getCountries(): List<SimpleCountry> {
        return apolloClient
            .query(CountriesQuery())
            .execute()
            .data
            ?.countries
            ?.map { it.toSimpleCountry() }
            ?.sortedBy { it.name }
            ?: emptyList()
    }

    override suspend fun getCountry(code: String): DetailedCountry? {
        return apolloClient
            .query(CountryQuery(code))
            .execute()
            .data
            ?.country
            ?.toDetailedCountry()
    }
}