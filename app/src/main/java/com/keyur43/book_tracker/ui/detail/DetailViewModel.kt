package com.keyur43.book_tracker.ui.detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import com.keyur43.book_tracker.data.Book
import com.keyur43.book_tracker.data.Progress
import com.keyur43.book_tracker.db.BooksDao
import com.keyur43.book_tracker.db.ProgressDao
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val booksDao: BooksDao,
    private val progressDao: ProgressDao
) : ViewModel() {

    var book by mutableStateOf(Book())
    var progress by mutableStateOf<List<Progress>>(emptyList())

    fun getBook(bookId: String) {
        viewModelScope.launch {
            booksDao.getBook(bookId).collect {
                if (it != null) book = it
                else throw IllegalArgumentException("Book not found")
            }
        }

        viewModelScope.launch {
            progressDao.getProgress(bookId).collect {
                progress = it
            }
        }
    }

    fun updateProgress(bookId: String, current: Int, total: Int, minutes: Int) {
        viewModelScope.launch {
            val book = booksDao.getBookOneShot(bookId) ?: return@launch
            if (book.currentPage < current) {
                progressDao.insertProgress(
                    Progress(
                        0,
                        bookId,
                        current - book.currentPage,
                        minutes,
                        System.currentTimeMillis()
                    )
                )
            }

            booksDao.updateBook(book.copy(currentPage = current, pageCount = total))
        }
    }
}