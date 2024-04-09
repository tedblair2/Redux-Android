package com.github.tedblair2.redux.model

import com.github.tedblair2.redux.service.State

data class CountryScreenState(
    val countries:List<SimpleCountry> = emptyList(),
    val isLoading:Boolean=true,
    val selectedCountry: DetailedCountry?=null
):State
