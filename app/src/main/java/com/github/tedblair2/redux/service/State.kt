package com.github.tedblair2.redux.service

interface State
interface Action

typealias Reducer<S> = (old:S,action:Action)->S
typealias StoreSubscriber<S> = (S)->Unit
typealias MiddleWare<S> = (store:Store<S>,action:Action,next:Next<S>)->Action
typealias Next<S> = (store:Store<S>,action:Action)->Action

fun<S : State> createStore(initialState: S,reducer:Reducer<S> , vararg middleWare: MiddleWare<S>):Store<S>{
   return AppStore(initialState,reducer, *middleWare)
}