package com.ogata_k.android_flux_todo_app.model

class Todo(private val id: Long, private val text: String, private var complete: Boolean): Cloneable, Comparable<Todo> {
    constructor(id: Long, text: String) : this(id, text, false)

    fun getId(): Long
    {
        return this.id
    }

    fun isComplete(): Boolean
    {
        return this.complete
    }

    fun setComplete(complete: Boolean)
    {
        this.complete = complete
    }

    fun getText(): String
    {
        return this.text
    }

    public override fun clone(): Todo {
        return Todo(id, text, complete)
    }

    override fun compareTo(other: Todo): Int {
        return when {
            id == other.getId() -> {
                0
            }
            id < other.getId() -> {
                -1
            }
            else -> {
                1
            }
        }
    }
}