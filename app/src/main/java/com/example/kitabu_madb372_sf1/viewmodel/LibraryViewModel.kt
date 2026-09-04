package com.example.kitabu_madb372_sf1.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.kitabu_madb372_sf1.data.*
import com.example.kitabu_madb372_sf1.repository.LibraryRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class LibraryViewModel(private val repository: LibraryRepository) : ViewModel() {

    private val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())

    val books: StateFlow<List<Book>> = repository.allBooks
        .map { entities ->
            entities.map { it.toUiModel() }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val bookings: StateFlow<List<Booking>> = combine(repository.allBookings, repository.allBooks) { bookingEntities, bookEntities ->
        bookingEntities.map { bookingEntity ->
            val book = bookEntities.find { it.bookId == bookingEntity.bookOwnerId }
            bookingEntity.toUiModel(book?.title ?: "Unknown", book?.author ?: "Unknown")
        }
    }
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun reserveBook(bookId: Long, durationDays: Int) {
        viewModelScope.launch {
            repository.reserveBook(bookId, durationDays)
        }
    }

    fun renewBooking(bookingId: Long) {
        viewModelScope.launch {
            repository.renewBooking(bookingId, 7) // Default 7 days extension
        }
    }

    fun returnBook(bookingId: Long) {
        viewModelScope.launch {
            repository.returnBook(bookingId)
        }
    }

    fun cancelBooking(bookingId: Long) {
        viewModelScope.launch {
            repository.cancelBooking(bookingId)
        }
    }

    private fun BookEntity.toUiModel() = Book(
        id = bookId,
        title = title,
        author = author,
        category = category,
        isAvailable = isAvailable
    )

    private fun BookingEntity.toUiModel(title: String, author: String): Booking {
        val now = System.currentTimeMillis()
        val remainingMillis = returnDeadline - now
        val remainingDays = (remainingMillis / (24 * 60 * 60 * 1000L)).toInt().coerceAtLeast(0)
        
        return Booking(
            id = bookingId,
            bookId = bookOwnerId,
            bookTitle = title,
            bookAuthor = author,
            bookingDate = dateFormat.format(Date(bookingDate)),
            returnDeadline = dateFormat.format(Date(returnDeadline)),
            daysRemaining = remainingDays,
            status = status.name
        )
    }
}

class LibraryViewModelFactory(private val repository: LibraryRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LibraryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LibraryViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
