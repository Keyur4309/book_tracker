package com.keyur43.book_tracker.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.keyur43.book_tracker.data.Progress
import kotlinx.coroutines.flow.Flow

@Dao
interface ProgressDao {

    @Insert
    suspend fun insertProgress(progress: Progress)

    @Query("SELECT * FROM progress WHERE bookId = :bookId")
    fun getProgress(bookId: String): Flow<List<Progress>>

    @Query("SELECT SUM(minutesRead) FROM progress WHERE date >= :weekStart")
    suspend fun getMinutesReadThisWeek(weekStart: Long): Int?

    @Query("SELECT SUM(pagesRead) FROM progress WHERE date >= :weekStart")
    suspend fun getPagesReadThisWeek(weekStart: Long): Int?

    @Query("DELETE FROM progress WHERE bookId = :bookId")
    suspend fun deleteBook(bookId: String)


}