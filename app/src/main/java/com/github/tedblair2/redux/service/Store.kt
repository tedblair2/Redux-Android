package com.github.tedblair2.redux.service

interface Store<S:State> {
    fun dispatch(action: Action)
    fun subscribe(subscriber: StoreSubscriber<S>):Boolean
    fun unsubscribe(subscriber: StoreSubscriber<S>):Boolean
}