package com.github.tedblair2.redux.action

import com.github.tedblair2.redux.model.DetailedCountry
import com.github.tedblair2.redux.model.SimpleCountry
import com.github.tedblair2.redux.service.Action

sealed interface CountriesAction:Action {
    data object GetCountries:CountriesAction
    data class CountriesResult(val list: List<SimpleCountry>):CountriesAction
    data class SelectCountry(val code:String):CountriesAction
    data class SelectedCountry(val country:DetailedCountry?=null):CountriesAction
    data object DismissDialog:CountriesAction
}