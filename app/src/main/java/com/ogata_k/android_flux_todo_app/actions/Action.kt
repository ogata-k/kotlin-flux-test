package com.ogata_k.android_flux_todo_app.actions


class Action internal constructor(
    private val type: String,
    private val data: HashMap<String, Any>
) {
    fun getType(): String {
        return this.type
    }

    fun getData(): HashMap<String, Any> {
        return this.data
    }

    companion object {
        fun new(type: String): Builder
        {
            return Builder(type)
        }


        class Builder(type: String){
            private val type: String
            private var data: HashMap<String, Any>

            init{
                this.type = type
                this.data = HashMap()
            }

            fun bundle(key: String, value: Any): Builder {
                data[key] = value
                return this
            }

            fun build(): Action {
                return Action(type, data)
            }
        }
    }
}