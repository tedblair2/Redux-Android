package com.github.tedblair2.redux.service

import com.github.tedblair2.redux.model.DetailedCountry
import com.github.tedblair2.redux.model.SimpleCountry

interface CountryService {
    suspend fun getCountries():List<SimpleCountry>
    suspend fun getCountry(code:String):DetailedCountry?
}