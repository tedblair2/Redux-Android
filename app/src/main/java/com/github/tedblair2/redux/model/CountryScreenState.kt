package com.github.tedblair2.redux.model

data class CountryScreenState(
    val countries:List<SimpleCountry> = emptyList(),
    val isLoading:Boolean=true,
    val selectedCountry: DetailedCountry?=null
)
