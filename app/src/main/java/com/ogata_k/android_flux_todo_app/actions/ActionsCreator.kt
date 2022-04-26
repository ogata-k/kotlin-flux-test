package com.ogata_k.android_flux_todo_app.actions

import com.ogata_k.android_flux_todo_app.dispatcher.Dispatcher
import com.ogata_k.android_flux_todo_app.model.Todo


class ActionsCreator(private val dispatcher: Dispatcher) {
    companion object {
        private var instance: ActionsCreator? = null
        operator fun get(dispatcher: Dispatcher): ActionsCreator {
            if (instance == null) {
                instance = ActionsCreator(dispatcher)
            }
            return instance!!
        }
    }

    fun create(text: String) {
        dispatcher.dispatch(
            TodoActions.TODO_CREATE,
            hashMapOf(TodoActions.KEY_TEXT to text)
        )
    }

    fun destroy(id: Long) {
        dispatcher.dispatch(
            TodoActions.TODO_DESTROY,
            hashMapOf(TodoActions.KEY_ID to id)
        )
    }

    fun undoDestroy() {
        dispatcher.dispatch(
            TodoActions.TODO_UNDO_DESTROY
        )
    }

    fun toggleComplete(todo: Todo) {
        val id = todo.getId()
        val actionType =
            if (todo.isComplete()) TodoActions.TODO_UNDO_COMPLETE else TodoActions.TODO_COMPLETE
        dispatcher.dispatch(
            actionType,
            hashMapOf(TodoActions.KEY_ID to id)
        )
    }

    fun toggleCompleteAll() {
        dispatcher.dispatch(TodoActions.TODO_TOGGLE_COMPLETE_ALL)
    }

    fun destroyCompleted() {
        dispatcher.dispatch(TodoActions.TODO_DESTROY_COMPLETED)
    }
}