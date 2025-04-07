package com.keyur43.book_tracker.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.keyur43.book_tracker.data.Book
import com.keyur43.book_tracker.data.Progress
import com.keyur43.book_tracker.util.Converters

@Database(entities = [Book::class, Progress::class], version = 1 ,exportSchema = false)
@TypeConverters(Converters::class)
abstract class ReadingTrackerDatabase : RoomDatabase() {
    abstract fun bookDao(): BooksDao
    abstract fun progressDao(): ProgressDao
}