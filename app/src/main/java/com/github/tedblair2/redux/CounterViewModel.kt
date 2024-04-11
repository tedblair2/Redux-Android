package com.github.tedblair2.redux

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.tedblair2.redux.action.AnotherCounterAction
import com.github.tedblair2.redux.model.AnotherCounterState
import com.github.tedblair2.redux.model.AppState
import com.github.tedblair2.redux.service.Dispatch
import com.github.tedblair2.redux.service.MiddleWare
import com.github.tedblair2.redux.service.Reducer
import com.github.tedblair2.redux.service.Store
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CounterViewModel @Inject constructor(
     store: Store
):ViewModel() {

    private var dispatch:Dispatch?=null

    val counterState=store.getCurrentState(subscriber = {dispatch=it})
        .map { it.anotherCounterState }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), AnotherCounterState())

    private val counterReducer:Reducer<AnotherCounterState> = { old , action->
        when(action){
            AnotherCounterAction.Increment->old.copy(count = old.count+1)
            AnotherCounterAction.Decrement->old.copy(count = old.count-1)
            is AnotherCounterAction.IncreaseByValue->old.copy(count = old.count+action.value)
            else-> old
        }
    }

    private val appReducer:Reducer<AppState> = { old , action ->
        old.copy(anotherCounterState = counterReducer(old.anotherCounterState,action))
    }

    private val middleWare:MiddleWare = { state , action, dispatch , next ->
        when(action){
            AnotherCounterAction.LoadData->{
                viewModelScope.launch {
                    val data=fakeAsyncCall()
                    dispatch(AnotherCounterAction.IncreaseByValue(data))
                }
                action
            }
            AnotherCounterAction.LoadNewData->{
                viewModelScope.launch {
                    val data=anotherFakeSyncCall()
                    dispatch(AnotherCounterAction.IncreaseByValue(data))
                }
                action
            }
            else->next(state, action, dispatch)
        }
    }

    init {
        store.applyReducer(appReducer)
            .applyMiddleWare(middleWare)
        dispatch?.invoke(AnotherCounterAction.Init)
    }

    fun onEvent(action: AnotherCounterAction){
        dispatch?.invoke(action)
    }

    private suspend fun fakeAsyncCall():Int{
        delay(700)
        return 30
    }

    private suspend fun anotherFakeSyncCall():Int{
        delay(700)
        return 50
    }

    override fun onCleared() {
        super.onCleared()
        dispatch=null
    }
}