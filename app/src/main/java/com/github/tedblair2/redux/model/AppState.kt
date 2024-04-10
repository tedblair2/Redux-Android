package com.github.tedblair2.redux.model

data class AppState(
    val anotherCounterState: AnotherCounterState= AnotherCounterState() ,
    val counterState: CounterState= CounterState() ,
    val countryScreenState: CountryScreenState= CountryScreenState()
)
