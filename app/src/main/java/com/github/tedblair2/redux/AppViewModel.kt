package com.github.tedblair2.redux

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.tedblair2.redux.action.CounterAction
import com.github.tedblair2.redux.model.CounterState
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
class AppViewModel @Inject constructor():ViewModel() {

    private val _counterState= MutableStateFlow(CounterState())
    val counterState=_counterState.asStateFlow()

    private val counterMiddleWare:MiddleWare<CounterState> = {store,action,next->
        when(action){
            CounterAction.LoadData->{
                viewModelScope.launch {
                    val data= fakeAsyncCall()
                    val newAction=CounterAction.DataLoaded(data)
                    store.dispatch(newAction)
                }
                action
            }
            else->next(store, action)
        }
    }

    private val counterReducer:Reducer<CounterState> = {old,action->
        when(action){
            CounterAction.Decrement->old.copy(count = old.count-1)
            CounterAction.Increment->old.copy(count = old.count+1)
            is CounterAction.DataLoaded->old.copy(count = old.count+action.value)
            else -> old
        }
    }

    private val store= createStore(CounterState(),counterReducer,counterMiddleWare)

    private suspend fun fakeAsyncCall():Int{
        delay(500)
        return 20
    }

    init {
        store.subscribe { current->
            _counterState.update {
                current
            }
        }
        store.dispatch(CounterAction.Init)
    }

    fun onEvent(counterAction: CounterAction){
        store.dispatch(counterAction)
    }
}