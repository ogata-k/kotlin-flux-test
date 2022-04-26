package com.ogata_k.android_flux_todo_app

import android.annotation.SuppressLint
import android.text.SpannableString
import android.text.style.StrikethroughSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ogata_k.android_flux_todo_app.actions.ActionsCreator
import com.ogata_k.android_flux_todo_app.model.Todo


class TodoRecyclerAdapter(actionsCreator: ActionsCreator): RecyclerView.Adapter<TodoRecyclerAdapter.ViewHolder>(){
    private var todos: List<Todo>

    init {
        this.todos = ArrayList()
        TodoRecyclerAdapter.actionsCreator = actionsCreator
    }

    companion object {
        private var actionsCreator: ActionsCreator? = null
    }

    class ViewHolder(v: View): RecyclerView.ViewHolder(v) {
        val todoText: TextView
        val todoCheck: CheckBox
        val todoDelete: Button

        init {
            todoText = v.findViewById(R.id.row_text) as TextView
            todoCheck = v.findViewById(R.id.row_checkbox) as CheckBox
            todoDelete = v.findViewById(R.id.row_delete) as Button
        }

        fun bindView(todo: Todo) {
            if (todo.isComplete()) {
                val spanString = SpannableString(todo.getText())
                spanString.setSpan(StrikethroughSpan(), 0, spanString.count(), 0);
                todoText.text = spanString;
            } else {
                todoText.text = todo.getText();
            }

            todoCheck.isChecked = todo.isComplete();
            todoCheck.setOnClickListener(object: View.OnClickListener {
                override fun onClick(view: View) {
                    actionsCreator?.toggleComplete(todo);
                }
            })

            todoDelete.setOnClickListener(object: View.OnClickListener{
                override fun onClick(view: View) {
                    actionsCreator?.destroy(todo.getId());
                }
            })
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, i: Int): ViewHolder {
        val v: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.todo_row_layout, parent, false);
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: TodoRecyclerAdapter.ViewHolder, position: Int) {
        holder.bindView(todos[position])
    }

    override fun getItemCount(): Int {
        return todos.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(todos: List<Todo>) {
        this.todos = todos
        notifyDataSetChanged()
    }

}