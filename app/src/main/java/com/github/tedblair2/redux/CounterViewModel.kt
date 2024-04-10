package com.github.tedblair2.redux

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.tedblair2.redux.action.AnotherCounterAction
import com.github.tedblair2.redux.model.AnotherCounterState
import com.github.tedblair2.redux.model.AppState
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
    private val store: Store
):ViewModel() {

    val counterState=store.getCurrentState()
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

    private val middleWare:MiddleWare = { store , action , next ->
        when(action){
            AnotherCounterAction.LoadData->{
                viewModelScope.launch {
                    val data=fakeAsyncCall()
                    store.dispatch(AnotherCounterAction.IncreaseByValue(data))
                }
                action
            }
            AnotherCounterAction.LoadNewData->{
                viewModelScope.launch {
                    val data=anotherFakeSyncCall()
                    store.dispatch(AnotherCounterAction.IncreaseByValue(data))
                }
                action
            }
            else->next(store, action)
        }
    }

    init {
        store.applyReducer(appReducer)
            .applyMiddleWare(middleWare)
            .dispatch(AnotherCounterAction.Init)
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