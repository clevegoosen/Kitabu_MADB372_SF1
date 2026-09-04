package com.example.kitabu_madb372_sf1.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.kitabu_madb372_sf1.data.Booking
import com.example.kitabu_madb372_sf1.ui.components.BookingCard

@Composable
fun ReservationsScreen(
    bookings: List<Booking>,
    onRenewClick: (Booking) -> Unit,
    onReturnClick: (Booking) -> Unit,
    onCancelClick: (Booking) -> Unit
) {

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Kitabu",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                )
                Text(
                    text = "My Bookings",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(top = 8.dp)
                )
                Text(
                    text = "Keep track of your library rentals.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            if (bookings.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
                    Text("No active bookings.", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {
                    items(bookings) { booking ->
                        BookingCard(
                            booking = booking,
                            onRenewClick = onRenewClick,
                            onReturnClick = onReturnClick,
                            onCancelClick = onCancelClick
                        )
                    }
                }
            }
        }
    }
}
