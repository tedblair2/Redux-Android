package com.github.tedblair2.redux

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.tedblair2.redux.action.CounterAction
import com.github.tedblair2.redux.model.AppState
import com.github.tedblair2.redux.model.CounterState
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
class AppViewModel @Inject constructor(
    private val store: Store
):ViewModel() {

    val counterState=store.getCurrentState()
        .map { it.counterState }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), CounterState())

    private val middleWare:MiddleWare = { state,action,dispatch,next->
        when(action){
            CounterAction.LoadData->{
                viewModelScope.launch {
                    val data= fakeAsyncCall()
                    val newAction=CounterAction.DataLoaded(data)
                    dispatch(newAction)
                }
                action
            }
            else->next(state, action, dispatch)
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

    private val appReducer:Reducer<AppState> ={ old , action ->
        old.copy(counterState = counterReducer(old.counterState,action))
    }

    private suspend fun fakeAsyncCall():Int{
        delay(500)
        return 20
    }

    init {
        store.applyReducer(appReducer)
            .applyMiddleWare(middleWare)
            .dispatch(CounterAction.Init)
    }

    fun onEvent(counterAction: CounterAction){
        store.dispatch(counterAction)
    }
}