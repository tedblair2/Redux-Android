package com.github.tedblair2.redux.service

import com.github.tedblair2.redux.model.AppState

interface Action

typealias Reducer<S> = (old:S,action:Action)->S
typealias Dispatch = (action:Action)->Unit
typealias MiddleWare = (state:AppState, action:Action,dispatch:Dispatch, next:Next)->Action
typealias Next = (state:AppState, action:Action,dispatch:Dispatch)->Action