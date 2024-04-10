package com.github.tedblair2.redux.service

import com.github.tedblair2.redux.model.AppState
import kotlinx.coroutines.flow.Flow

interface Store{
    fun applyMiddleWare(middleWare: MiddleWare):Store
    fun applyReducer(reducer: Reducer<AppState>):Store
    fun getCurrentState(dispatcher:(Dispatch)->Unit): Flow<AppState>
}