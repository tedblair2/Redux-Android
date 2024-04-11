package com.github.tedblair2.redux

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.tedblair2.redux.action.CountriesAction
import com.github.tedblair2.redux.model.AppState
import com.github.tedblair2.redux.model.CountryScreenState
import com.github.tedblair2.redux.service.CountryService
import com.github.tedblair2.redux.service.Dispatch
import com.github.tedblair2.redux.service.MiddleWare
import com.github.tedblair2.redux.service.Reducer
import com.github.tedblair2.redux.service.Store
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CountriesViewModel @Inject constructor(
    store: Store,
    private val countryService: CountryService
):ViewModel() {

    private var dispatch:Dispatch?=null
    val countryState=store.getCurrentState(subscriber = {dispatch=it})
        .map { it.countryScreenState }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000),CountryScreenState())

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

    private val appStateReducer:Reducer<AppState> = {old, action ->
        old.copy(countryScreenState = reducer(old.countryScreenState,action))
    }

    private val middleWare:MiddleWare = {state, action, dispatch, next ->
        when(action){
            CountriesAction.GetCountries->{
                viewModelScope.launch {
                    val countries=countryService.getCountries()
                    dispatch(CountriesAction.CountriesResult(countries))
                }
                action
            }
            is CountriesAction.SelectCountry->{
                viewModelScope.launch {
                    val country=countryService.getCountry(action.code)
                    dispatch(CountriesAction.SelectedCountry(country))
                }
                action
            }
            else->next(state, action, dispatch)
        }
    }

    init {
        store.applyReducer(appStateReducer)
            .applyMiddleWare(middleWare)
        dispatch?.invoke(CountriesAction.GetCountries)
    }

    fun onEvent(action: CountriesAction){
        dispatch?.invoke(action)
    }

    override fun onCleared() {
        super.onCleared()
        dispatch=null
    }
}