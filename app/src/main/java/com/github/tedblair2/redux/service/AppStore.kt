package com.github.tedblair2.redux.service

import com.github.tedblair2.redux.model.AppState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class AppStore @Inject constructor() : Store {

    private val reducers= mutableSetOf<Reducer<AppState>>()
    private val middleWares= mutableSetOf<MiddleWare>()
    private val _appState= MutableStateFlow(AppState())
    private val appState=_appState.asStateFlow()

    override fun dispatch(action: Action) {
        val newAction=applyMiddleWares(action)
//        var currentState=appState.value
//        reducers.forEach {
//            currentState=it(currentState,newAction)
//        }
//        if (currentState==appState.value){
//            return
//        }
//        _appState.update {
//            currentState
//        }
        val newState=reducers.fold(appState.value){ currentState , reducer->
            reducer(currentState, newAction)
        }
        if (newState==appState.value){
            return
        }
        _appState.update {
            newState
        }
    }

    override fun applyMiddleWare(middleWare: MiddleWare): Store {
        middleWares.add(middleWare)
        return this
    }

    override fun applyReducer(reducer: Reducer<AppState>): Store {
        reducers.add(reducer)
        return this
    }

    override fun getCurrentState(): Flow<AppState> {
        return appState
    }

    private fun applyMiddleWares(action: Action):Action{
        return next(0)(appState.value,action,::dispatch)
    }

    private fun next(index:Int):Next{
        if (index==middleWares.size){
            return {_,action,_->action}
        }
        return {state, action,dispatch ->
            middleWares.toList()[index].invoke(state,action,dispatch,next(index+1))
        }
    }
}