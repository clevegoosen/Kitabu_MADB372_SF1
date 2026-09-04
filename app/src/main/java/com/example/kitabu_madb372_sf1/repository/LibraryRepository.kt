package com.example.kitabu_madb372_sf1.repository

import com.example.kitabu_madb372_sf1.data.*
import kotlinx.coroutines.flow.Flow

class LibraryRepository(
    private val bookDao: BookDao,
    private val bookingDao: BookingDao
) {
    val allBooks: Flow<List<BookEntity>> = bookDao.getAllBooks()
    val allBookings: Flow<List<BookingEntity>> = bookingDao.getAllBookings()

    suspend fun reserveBook(bookId: Long, durationDays: Int) {
        val book = bookDao.getBookById(bookId)
        if (book != null && book.isAvailable) {
            val now = System.currentTimeMillis()
            val deadline = now + (durationDays * 24 * 60 * 60 * 1000L)
            
            val booking = BookingEntity(
                bookOwnerId = bookId,
                userName = "Current User",
                bookingDate = now,
                returnDeadline = deadline,
                status = BookingStatus.ACTIVE
            )
            
            bookingDao.insertBooking(booking)
            bookDao.updateBook(book.copy(isAvailable = false))
        }
    }

    suspend fun renewBooking(bookingId: Long, additionalDays: Int) {
        val booking = bookingDao.getBookingById(bookingId)
        if (booking != null && booking.status == BookingStatus.ACTIVE) {
            val newDeadline = booking.returnDeadline + (additionalDays * 24 * 60 * 60 * 1000L)
            bookingDao.updateBooking(booking.copy(returnDeadline = newDeadline))
        }
    }

    suspend fun returnBook(bookingId: Long) {
        val booking = bookingDao.getBookingById(bookingId)
        if (booking != null) {
            bookingDao.updateBooking(booking.copy(status = BookingStatus.RETURNED))
            val book = bookDao.getBookById(booking.bookOwnerId)
            if (book != null) {
                bookDao.updateBook(book.copy(isAvailable = true))
            }
        }
    }

    suspend fun cancelBooking(bookingId: Long) {
        val booking = bookingDao.getBookingById(bookingId)
        if (booking != null) {
            bookingDao.deleteBooking(booking)
            val book = bookDao.getBookById(booking.bookOwnerId)
            if (book != null) {
                bookDao.updateBook(book.copy(isAvailable = true))
            }
        }
    }
}
