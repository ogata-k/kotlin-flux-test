package com.ogata_k.android_flux_todo_app.actions

interface TodoActions {
    companion object {
        const val TODO_CREATE: String = "todo-create"
        const val TODO_COMPLETE: String = "todo-complete"
        const val TODO_DESTROY: String = "todo-destroy"
        const val TODO_DESTROY_COMPLETED: String = "todo-destroy-completed"
        const val TODO_TOGGLE_COMPLETE_ALL: String = "todo-toggle-complete-all"
        const val TODO_UNDO_COMPLETE: String = "todo-undo-complete"
        const val TODO_UNDO_DESTROY: String = "todo-undo-destroy"
        const val KEY_TEXT: String = "key-text"
        const val KEY_ID: String = "key-id"
    }
}