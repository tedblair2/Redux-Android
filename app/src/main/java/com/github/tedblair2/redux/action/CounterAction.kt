package com.github.tedblair2.redux.action

import com.github.tedblair2.redux.service.Action

sealed interface CounterAction:Action {
    data object Init:CounterAction
    data object Increment:CounterAction
    data object Decrement:CounterAction
    data object LoadData:CounterAction
    data class DataLoaded(val value:Int):CounterAction
}

sealed interface AnotherCounterAction:Action {
    data object Init:AnotherCounterAction
    data object Increment:AnotherCounterAction
    data object Decrement:AnotherCounterAction
    data object LoadData:AnotherCounterAction
    data object LoadNewData:AnotherCounterAction
    data class IncreaseByValue(val value: Int):AnotherCounterAction
}