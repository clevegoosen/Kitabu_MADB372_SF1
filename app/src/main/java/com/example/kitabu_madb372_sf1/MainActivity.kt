package com.example.kitabu_madb372_sf1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.kitabu_madb372_sf1.data.LibraryDatabase
import com.example.kitabu_madb372_sf1.repository.LibraryRepository
import com.example.kitabu_madb372_sf1.ui.navigation.AppNavigation
import com.example.kitabu_madb372_sf1.ui.theme.KitabuTheme
import com.example.kitabu_madb372_sf1.viewmodel.LibraryViewModel
import com.example.kitabu_madb372_sf1.viewmodel.LibraryViewModelFactory

class MainActivity : ComponentActivity() {
    
    private val database by lazy { LibraryDatabase.getDatabase(this, lifecycleScope) }
    private val repository by lazy { LibraryRepository(database.bookDao(), database.bookingDao()) }
    private val viewModel: LibraryViewModel by viewModels {
        LibraryViewModelFactory(repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KitabuTheme {
                AppNavigation(viewModel)
            }
        }
    }
}
