package com.example.lance.bookbrowser

class Database {
    fun searchBooks(query: String) : List<Book> {
       val result = ArrayList<Book>();
        result.add(Book(1, "The Hobbit: ${query}", "J.R.R. Tolkien", 2.14));
        result.add(Book(2, "The Fellowship of the Ring", "J.R.R. Tolkien", 4.93));
        result.add(Book(3, "The Two Towers", "J.R.R. Tolkien", 6.01));
        result.add(Book(4, "The Return of the King", "J.R.R. Tolkien", 5.73));
        return result
    }
}