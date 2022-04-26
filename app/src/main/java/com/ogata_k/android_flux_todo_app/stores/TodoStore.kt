package com.ogata_k.android_flux_todo_app.stores

import com.ogata_k.android_flux_todo_app.actions.Action
import com.ogata_k.android_flux_todo_app.actions.TodoActions
import com.ogata_k.android_flux_todo_app.dispatcher.Dispatcher
import com.ogata_k.android_flux_todo_app.model.Todo
import com.squareup.otto.Subscribe

class TodoStore(dispatcher: Dispatcher) : Store(dispatcher) {
    private val todos: MutableList<Todo>
    private var lastDeleted: Todo?

    init {
        this.todos = mutableListOf()
        this.lastDeleted = null;
    }

    companion object {
        private var instance: TodoStore? = null;

        fun get(dispatcher: Dispatcher): TodoStore {
            if (this.instance == null) {
                this.instance = TodoStore(dispatcher)
            }

            return this.instance!!
        }
    }

    fun getTodos(): List<Todo> {
        return todos
    }

    fun canUndo(): Boolean {
        return lastDeleted != null
    }

    class TodoStoreChangeEvent : StoreChangeEvent

    override fun changeEvent(): StoreChangeEvent {
        return TodoStoreChangeEvent()
    }

    @Subscribe
    override fun onAction(action: Action) {
        when (action.getType()) {
            TodoActions.TODO_CREATE -> {
                val text: String = action.getData()[TodoActions.KEY_TEXT] as String
                create(text)
                emitStoreChange()
            }
            TodoActions.TODO_DESTROY -> {
                val id: Long = action.getData()[TodoActions.KEY_ID] as Long
                destroy(id)
                emitStoreChange()
            }
            TodoActions.TODO_UNDO_DESTROY -> {
                undoDestroy()
                emitStoreChange()
            }
            TodoActions.TODO_COMPLETE -> {
                val id: Long = action.getData()[TodoActions.KEY_ID] as Long
                updateComplete(id, true)
                emitStoreChange()
            }
            TodoActions.TODO_UNDO_COMPLETE -> {
                val id: Long = action.getData()[TodoActions.KEY_ID] as Long
                updateComplete(id, false)
                emitStoreChange()
            }
            TodoActions.TODO_DESTROY_COMPLETED -> {
                destroyCompleted()
                emitStoreChange()
            }
            TodoActions.TODO_TOGGLE_COMPLETE_ALL -> {
                updateCompleteAll()
                emitStoreChange()
            }
        }
    }

    private fun destroyCompleted() {
        todos.removeAll {
            it.isComplete()
        }
    }

    private fun updateCompleteAll() {
        if (areAllComplete()) {
            updateAllComplete(false)
        } else {
            updateAllComplete(true)
        }
    }

    private fun areAllComplete(): Boolean {
        return todos.all {
            it.isComplete()
        }
    }

    private fun updateAllComplete(complete: Boolean) {
        for (todo in todos) {
            todo.setComplete(complete)
        }
    }

    private fun updateComplete(id: Long, complete: Boolean) {
        val todo = getById(id)
        todo?.setComplete(complete)
    }

    private fun undoDestroy() {
        if (lastDeleted != null) {
            addElement(lastDeleted!!.clone())
            lastDeleted = null
        }
    }

    private fun create(text: String) {
        val id = System.currentTimeMillis()
        val todo = Todo(id, text)
        addElement(todo)
    }

    private fun destroy(id: Long) {
        todos.removeAll {
            if (it.getId() == id) {
                lastDeleted = it.clone()
                true
            } else {
                false
            }
        }
    }

    private fun getById(id: Long): Todo? {
        for (todo in todos) {
            if (todo.getId() == id) {
                return todo
            }
        }
        return null
    }


    private fun addElement(clone: Todo) {
        todos.add(clone)
        todos.sort()
    }
}