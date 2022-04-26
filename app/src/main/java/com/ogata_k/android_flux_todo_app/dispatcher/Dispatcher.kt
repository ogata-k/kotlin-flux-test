package com.ogata_k.android_flux_todo_app.dispatcher

import com.ogata_k.android_flux_todo_app.actions.Action
import com.ogata_k.android_flux_todo_app.stores.Store
import com.squareup.otto.Bus

class Dispatcher(private val bus: Bus) {
    companion object {
        private var instance: Dispatcher? = null
        fun get(bus: Bus): Dispatcher {
            if (instance == null) {
                instance = Dispatcher(bus);
            }

            return this.instance!!
        }
    }

    fun register(cls: Any)
    {
        bus.register(cls)
    }

    fun unregister(cls: Any)
    {
        bus.unregister(cls)
    }

    fun emitChange(o: Store.StoreChangeEvent)
    {
        post(o)
    }

    fun dispatch(type: String, data: HashMap<String, Any> = HashMap()) {
        require(!isEmpty(type)) { "Type must not be empty" }

        val actionBuilder: Action.Companion.Builder = Action.new(type)
        for((key, value) in data){
            actionBuilder.bundle(key, value)
        }

        post(actionBuilder.build())
    }

    private fun isEmpty(type: String?): Boolean {
        return type == null || type.isEmpty()
    }

    private fun post(event: Any) {
        bus.post(event)
    }
}