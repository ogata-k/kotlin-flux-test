package com.ogata_k.android_flux_todo_app

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.ogata_k.android_flux_todo_app.actions.ActionsCreator
import com.ogata_k.android_flux_todo_app.dispatcher.Dispatcher
import com.ogata_k.android_flux_todo_app.stores.TodoStore
import com.ogata_k.android_flux_todo_app.stores.TodoStore.TodoStoreChangeEvent
import com.squareup.otto.Bus
import com.squareup.otto.Subscribe

class TodoActivity(): AppCompatActivity() {
    private var mainInput: EditText? = null
    private var mainLayout: ViewGroup? = null
    private var dispatcher: Dispatcher? = null
    private var actionsCreator: ActionsCreator? = null
    private var todoStore: TodoStore? = null
    private var listAdapter: TodoRecyclerAdapter? = null
    private var mainCheck: CheckBox? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initDependencies()
        setupView()
    }

    private fun initDependencies() {
        dispatcher = Dispatcher.get(Bus())
        actionsCreator = ActionsCreator[dispatcher!!]
        todoStore = TodoStore.get(dispatcher!!)
    }

    private fun setupView() {
        mainLayout = findViewById<View>(R.id.main_layout) as ViewGroup
        mainInput = findViewById<View>(R.id.main_input) as EditText
        val mainAdd = findViewById<View>(R.id.main_add) as Button
        mainAdd.setOnClickListener {
            addTodo()
            resetMainInput()
        }
        mainCheck = findViewById<View>(R.id.main_checkbox) as CheckBox
        mainCheck!!.setOnClickListener { checkAll() }
        val mainClearCompleted = findViewById<View>(R.id.main_clear_completed) as Button
        mainClearCompleted.setOnClickListener {
            clearCompleted()
            resetMainCheck()
        }
        val mainList = findViewById<View>(R.id.main_list) as RecyclerView
        mainList.layoutManager = LinearLayoutManager(this)
        listAdapter = TodoRecyclerAdapter(actionsCreator!!)
        mainList.adapter = listAdapter
    }

    private fun updateUI() {
        listAdapter!!.setItems(todoStore!!.getTodos())
        if (todoStore!!.canUndo()) {
            val snackbar = Snackbar.make(mainLayout!!, "Element deleted", Snackbar.LENGTH_LONG)
            snackbar.setAction(
                "Undo"
            ) { actionsCreator!!.undoDestroy() }
            snackbar.show()
        }
    }

    override fun onResume() {
        super.onResume()
        dispatcher!!.register(this)
        dispatcher!!.register(todoStore!!)
    }

    override fun onPause() {
        super.onPause()
        dispatcher!!.unregister(this)
        dispatcher!!.unregister(todoStore!!)
    }

    private fun addTodo() {
        if (validateInput()) {
            actionsCreator!!.create(getInputText())
        }
    }

    private fun checkAll() {
        actionsCreator!!.toggleCompleteAll()
    }

    private fun clearCompleted() {
        actionsCreator!!.destroyCompleted()
    }

    private fun resetMainInput() {
        mainInput!!.setText("")
    }

    private fun resetMainCheck() {
        if (mainCheck!!.isChecked) {
            mainCheck!!.isChecked = false
        }
    }

    private fun validateInput(): Boolean {
        return !TextUtils.isEmpty(getInputText())
    }

    private fun getInputText(): String {
        return mainInput!!.text.toString()
    }

    @Subscribe
    fun onTodoStoreChange(event: TodoStoreChangeEvent?) {
        updateUI()
    }
}