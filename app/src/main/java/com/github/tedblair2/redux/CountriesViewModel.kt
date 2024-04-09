package com.github.tedblair2.redux

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.tedblair2.redux.action.CountriesAction
import com.github.tedblair2.redux.model.CountryScreenState
import com.github.tedblair2.redux.service.CountryService
import com.github.tedblair2.redux.service.MiddleWare
import com.github.tedblair2.redux.service.Reducer
import com.github.tedblair2.redux.service.createStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CountriesViewModel @Inject constructor(
    private val countryService: CountryService
):ViewModel() {

    private val _countryState= MutableStateFlow(CountryScreenState())
    val countryState=_countryState.asStateFlow()

    private val reducer:Reducer<CountryScreenState> = {old, action ->
        when(action){
            is CountriesAction.CountriesResult->{
                old.copy(countries = action.list,isLoading = false)
            }
            is CountriesAction.SelectedCountry->{
                old.copy(selectedCountry = action.country)
            }
            CountriesAction.DismissDialog->{
                old.copy(selectedCountry = null)
            }
            else->old
        }
    }

    private val middleWare:MiddleWare<CountryScreenState> = {store, action, next ->
        when(action){
            CountriesAction.GetCountries->{
                viewModelScope.launch {
                    val countries=countryService.getCountries()
                    store.dispatch(CountriesAction.CountriesResult(countries))
                }
                action
            }
            is CountriesAction.SelectCountry->{
                viewModelScope.launch {
                    val country=countryService.getCountry(action.code)
                    store.dispatch(CountriesAction.SelectedCountry(country))
                }
                action
            }
            else->next(store, action)
        }
    }

    private val store= createStore(CountryScreenState(),reducer,middleWare)

    init {
        store.subscribe { currentState->
            _countryState.update {
                currentState
            }
        }
        store.dispatch(CountriesAction.GetCountries)
    }

    fun onEvent(action: CountriesAction){
        store.dispatch(action)
    }
}