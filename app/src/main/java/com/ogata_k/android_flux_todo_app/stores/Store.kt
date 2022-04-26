package com.ogata_k.android_flux_todo_app.stores

import com.ogata_k.android_flux_todo_app.actions.Action
import com.ogata_k.android_flux_todo_app.dispatcher.Dispatcher

abstract class Store(protected val dispatcher: Dispatcher) {
    fun emitStoreChange()
    {
        dispatcher.emitChange(changeEvent())
    }

    abstract fun changeEvent(): StoreChangeEvent

    abstract fun onAction(action: Action)

    interface StoreChangeEvent{}
}