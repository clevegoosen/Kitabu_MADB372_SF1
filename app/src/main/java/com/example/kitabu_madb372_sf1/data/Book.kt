package com.example.kitabu_madb372_sf1.data

data class Book(
    val id: Long = 0,
    val title: String,
    val author: String,
    val category: String,
    val isAvailable: Boolean = true
)
