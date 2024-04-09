package com.github.tedblair2.redux

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.tedblair2.redux.action.AnotherCounterAction
import com.github.tedblair2.redux.model.AnotherCounterState
import com.github.tedblair2.redux.service.MiddleWare
import com.github.tedblair2.redux.service.Reducer
import com.github.tedblair2.redux.service.createStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CounterViewModel @Inject constructor():ViewModel() {

    private val _counterState= MutableStateFlow(AnotherCounterState())
    val counterState=_counterState.asStateFlow()

    private val reducer:Reducer<AnotherCounterState> = {old,action->
        when(action){
            AnotherCounterAction.Increment->old.copy(count = old.count+1)
            AnotherCounterAction.Decrement->old.copy(count = old.count-1)
            is AnotherCounterAction.IncreaseByValue->old.copy(count = old.count+action.value)
            else-> old
        }
    }

    private val middleWare:MiddleWare<AnotherCounterState> = {store, action, next ->
        when(action){
            AnotherCounterAction.LoadData->{
                viewModelScope.launch {
                    val data=fakeAsyncCall()
                    store.dispatch(AnotherCounterAction.IncreaseByValue(data))
                }
                action
            }
            else->next(store, action)
        }
    }

    private val anotherMiddleWare:MiddleWare<AnotherCounterState> = {store, action, next ->
        when(action){
            is AnotherCounterAction.LoadNewData->{
                viewModelScope.launch {
                    val data=anotherFakeSyncCall()
                    store.dispatch(AnotherCounterAction.IncreaseByValue(data))
                }
                action
            }
            else->next(store, action)
        }
    }

    private val store= createStore(AnotherCounterState(),reducer,middleWare,anotherMiddleWare)

    init {
        store.subscribe {currentState->
            _counterState.update {
                currentState
            }
        }
        store.dispatch(AnotherCounterAction.Init)
    }

    fun onEvent(action: AnotherCounterAction){
        store.dispatch(action)
    }

    private suspend fun fakeAsyncCall():Int{
        delay(700)
        return 30
    }

    private suspend fun anotherFakeSyncCall():Int{
        delay(700)
        return 50
    }
}