package com.example.lance.bookbrowser

data class User(var cart: MutableList <Book>, var email : String, var picture : String,
                var successful_sales : Long, var thumbs_up : Long, var thumbs_down : Long,
                var username : String)
