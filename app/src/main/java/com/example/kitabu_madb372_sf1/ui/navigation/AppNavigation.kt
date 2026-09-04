package com.example.kitabu_madb372_sf1.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.kitabu_madb372_sf1.data.Book
import com.example.kitabu_madb372_sf1.ui.components.ReservationDialog
import com.example.kitabu_madb372_sf1.ui.screens.CatalogScreen
import com.example.kitabu_madb372_sf1.ui.screens.ReservationsScreen
import com.example.kitabu_madb372_sf1.viewmodel.LibraryViewModel

@Composable
fun AppNavigation(viewModel: LibraryViewModel) {
    val navController = rememberNavController()
    var selectedItem by remember { mutableIntStateOf(0) }
    val items = listOf("Catalog", "My Books")
    val icons = listOf(Icons.Default.Book, Icons.Default.Bookmark)
    
    val books by viewModel.books.collectAsStateWithLifecycle()
    val bookings by viewModel.bookings.collectAsStateWithLifecycle()
    
    var showReservationDialog by remember { mutableStateOf(false) }
    var selectedBookForReservation by remember { mutableStateOf<Book?>(null) }

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.primary
            ) {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = { Icon(icons[index], contentDescription = item) },
                        label = { Text(item) },
                        selected = selectedItem == index,
                        onClick = {
                            selectedItem = index
                            if (index == 0) {
                                navController.navigate("catalog") {
                                    popUpTo("catalog") { inclusive = true }
                                }
                            } else {
                                navController.navigate("bookings") {
                                    popUpTo("catalog")
                                }
                            }
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            indicatorColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    )
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = "catalog",
            modifier = Modifier.padding(paddingValues)
        ) {
            composable("catalog") {
                CatalogScreen(
                    books = books,
                    onReserveClick = { book ->
                        selectedBookForReservation = book
                        showReservationDialog = true
                    }
                )
            }
            composable("bookings") {
                ReservationsScreen(
                    bookings = bookings,
                    onRenewClick = { booking -> viewModel.renewBooking(booking.id) },
                    onReturnClick = { booking -> viewModel.returnBook(booking.id) },
                    onCancelClick = { booking -> viewModel.cancelBooking(booking.id) }
                )
            }
        }
        
        if (showReservationDialog && selectedBookForReservation != null) {
            ReservationDialog(
                book = selectedBookForReservation!!,
                onDismiss = { showReservationDialog = false },
                onConfirm = { duration ->
                    viewModel.reserveBook(selectedBookForReservation!!.id, duration)
                    showReservationDialog = false
                }
            )
        }
    }
}
