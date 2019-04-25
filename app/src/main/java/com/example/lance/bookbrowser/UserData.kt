package com.example.lance.bookbrowser

object UserData {
    private var data: String? = null
    fun getData(): String? {
        return data
    }

    fun setData(data: String): Boolean{
        println("this is for changing purposes. delete this println")
        UserData.data = data
        return true
    }
}
