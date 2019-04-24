package com.example.lance.bookbrowser

data class Order(var time_placed: String, var date_placed: String, var total: Double, var store: String,
                 var user : String?, var books: MutableList <Book>)