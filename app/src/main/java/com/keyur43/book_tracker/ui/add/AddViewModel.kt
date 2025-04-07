package com.keyur43.book_tracker.ui.add

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keyur43.book_tracker.data.Book
import com.keyur43.book_tracker.data.toBook
import com.keyur43.book_tracker.db.BooksDao
import com.keyur43.book_tracker.network.GoogleBooksService
import com.keyur43.book_tracker.util.UiState
import com.keyur43.book_tracker.util.networkResult
import com.keyur43.book_tracker.util.replaceWith
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
    class AddViewModel @Inject constructor(
    private val api: GoogleBooksService,
    private val booksDao: BooksDao
) : ViewModel() {
    var searchQuery by mutableStateOf("")
    val googleBooksSearchResult = mutableStateListOf<Book>()

    var isAddingCustomBook by mutableStateOf(false)

    private var searchJob: Job? = null


    fun searchGoogleBooks(query: String) {
        searchJob?.cancel()
        searchQuery = query
        searchJob = viewModelScope.launch {
            val result = networkResult { api.getBooks(query) }
            if (result is UiState.Success) {
                googleBooksSearchResult.replaceWith(result.data.items.map { it.toBook() })
            }
        }
    }

    fun addBook(book: Book) {
        viewModelScope.launch {
            booksDao.insertBook(book)
        }
    }

    fun clearSearchState() {
        searchQuery = ""
        googleBooksSearchResult.clear()
        isAddingCustomBook = false
        searchJob?.cancel()
        searchJob = null
    }

    fun addCustomBook(book: Book) {
        viewModelScope.launch {
            booksDao.insertBook(
                book.copy(
                    id = book.title + book.authors.joinToString(",") + book.pageCount,
                )
            )
        }
    }
}