package com.keyur43.book_tracker.ui.home

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import com.keyur43.book_tracker.data.Book
import com.keyur43.book_tracker.db.BooksDao
import com.keyur43.book_tracker.db.ProgressDao
import com.keyur43.book_tracker.util.replaceWith
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val booksDao: BooksDao,
    private val progressDao: ProgressDao
) : ViewModel() {

    init {
        getBooks()
    }

    val books = mutableStateListOf<Book>()

    private fun getBooks() {
        viewModelScope.launch {
            booksDao.getBooks().collect {
                books.replaceWith(it)
            }
        }
    }

    fun removeBook(book: Book) {
        viewModelScope.launch {
            booksDao.deleteBook(book)
            progressDao.deleteBook(book.id)
        }
    }
}