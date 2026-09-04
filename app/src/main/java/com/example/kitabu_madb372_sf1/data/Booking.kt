package com.example.kitabu_madb372_sf1.data

data class Booking(
    val id: Long = 0,
    val bookId: Long,
    val bookTitle: String,
    val bookAuthor: String,
    val bookingDate: String,
    val returnDeadline: String,
    val daysRemaining: Int,
    val status: String // PENDING, ACTIVE, RETURNED
)
