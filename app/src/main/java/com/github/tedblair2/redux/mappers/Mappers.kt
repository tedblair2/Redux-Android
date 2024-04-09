package com.github.tedblair2.redux.mappers

import com.github.tedblair2.CountriesQuery
import com.github.tedblair2.CountryQuery
import com.github.tedblair2.redux.model.DetailedCountry
import com.github.tedblair2.redux.model.SimpleCountry

fun CountriesQuery.Country.toSimpleCountry(): SimpleCountry {
    return SimpleCountry(
        code = code,
        name = name,
        emoji = emoji,
        capital = capital ?: ""
    )
}

fun CountryQuery.Country.toDetailedCountry(): DetailedCountry {
    return DetailedCountry(
        code = code,
        name = name,
        currency = currency ?: "",
        capital = capital ?: "",
        languages = languages.joinToString(","){it.name},
        emoji = emoji,
        continent = continent.name
    )
}