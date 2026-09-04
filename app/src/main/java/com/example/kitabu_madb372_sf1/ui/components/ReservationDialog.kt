package com.example.kitabu_madb372_sf1.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kitabu_madb372_sf1.data.Book
import com.example.kitabu_madb372_sf1.ui.theme.KitabuPurple

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReservationDialog(
    book: Book,
    onDismiss: () -> Unit,
    onConfirm: (Int) -> Unit
) {
    var selectedDuration by remember { mutableStateOf(7) }
    val durations = listOf(7, 14, 21)
    
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(),
        containerColor = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
                .padding(bottom = 32.dp)
        ) {
            Text(
                text = "Reserve Book",
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = book.title,
                style = MaterialTheme.typography.titleLarge,
                color = KitabuPurple
            )
            Text(
                text = book.author,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Text(
                text = "Rental duration",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            durations.forEach { duration ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .selectable(
                            selected = (duration == selectedDuration),
                            onClick = { selectedDuration = duration }
                        )
                        .padding(vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = (duration == selectedDuration),
                        onClick = { selectedDuration = duration }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "$duration days", style = MaterialTheme.typography.bodyLarge)
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedButton(
                    onClick = onDismiss,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Cancel")
                }
                Button(
                    onClick = { onConfirm(selectedDuration) },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = KitabuPurple)
                ) {
                    Text("Reserve")
                }
            }
        }
    }
}
