package com.example.kitabu_madb372_sf1.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [BookEntity::class, BookingEntity::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class LibraryDatabase : RoomDatabase() {
    abstract fun bookDao(): BookDao
    abstract fun bookingDao(): BookingDao

    companion object {
        @Volatile
        private var INSTANCE: LibraryDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): LibraryDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    LibraryDatabase::class.java,
                    "library_database"
                )
                .addCallback(LibraryDatabaseCallback(scope))
                .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class LibraryDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch(Dispatchers.IO) {
                    populateDatabase(database.bookDao())
                }
            }
        }

        suspend fun populateDatabase(bookDao: BookDao) {
            bookDao.insertBook(BookEntity(title = "Clean Code", author = "Robert C. Martin", category = "Programming", isAvailable = true))
            bookDao.insertBook(BookEntity(title = "Kotlin in Action", author = "Dmitry Jemerov", category = "Programming", isAvailable = true))
            bookDao.insertBook(BookEntity(title = "Database System Concepts", author = "Abraham Silberschatz", category = "Database", isAvailable = true))
            bookDao.insertBook(BookEntity(title = "Things Fall Apart", author = "Chinua Achebe", category = "Literature", isAvailable = true))
            bookDao.insertBook(BookEntity(title = "The Pragmatic Programmer", author = "David Thomas", category = "Programming", isAvailable = true))
        }
    }
}
