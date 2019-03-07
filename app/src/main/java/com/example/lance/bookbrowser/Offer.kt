package com.example.lance.bookbrowser

import com.example.lance.bookbrowser.Book

data class Offer(var time_placed: String, var date_placed: String, var asking_price: Double, var seller: String,
                 var book: Book, var note: String)