package com.github.tedblair2.redux.service

interface Action

typealias Reducer<S> = (old:S,action:Action)->S
typealias MiddleWare = (store:Store , action:Action , next:Next)->Action
typealias Next = (store:Store , action:Action)->Action