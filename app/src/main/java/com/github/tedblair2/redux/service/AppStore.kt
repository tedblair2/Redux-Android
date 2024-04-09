package com.github.tedblair2.redux.service

class AppStore<S : State> (
    initialState: S,
    private val reducer: Reducer<S>,
    vararg middleWare:MiddleWare<S>
): Store<S> {

    private val subscribers= mutableSetOf<StoreSubscriber<S>>()
    private val middleWares= middleWare

    private var state:S = initialState
        set(value) {
            field=value
            subscribers.forEach { it(value) }
        }

    override fun dispatch(action: Action) {
        val newAction=applyMiddleWare(action)
        val newState=reducer(state,newAction)
        if (newState==state){
            return
        }
        state=newState
    }

    override fun subscribe(subscriber: StoreSubscriber<S>)=subscribers.add(element = subscriber)

    override fun unsubscribe(subscriber: StoreSubscriber<S>)=subscribers.remove(element = subscriber)

    private fun applyMiddleWare(action: Action):Action{
        return next(0)(this,action)
    }

    private fun next(index:Int):Next<S>{
        if (index==middleWares.size){
            return {_,action->action}
        }
        return {store, action ->
            middleWares[index].invoke(store,action,next(index+1))
        }
    }
}