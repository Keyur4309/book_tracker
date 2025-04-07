package com.keyur43.book_tracker.ui.recent

import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keyur43.book_tracker.data.Book
import com.keyur43.book_tracker.data.toBook
import com.keyur43.book_tracker.db.BooksDao
import com.keyur43.book_tracker.network.GoogleBooksService
import com.keyur43.book_tracker.util.UiState
import com.keyur43.book_tracker.util.networkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookViewModel @Inject constructor(
    private val api: GoogleBooksService,
    private val booksDao: BooksDao
) : ViewModel() {

    val categories = listOf(
        "Fantasy", "Science Fiction", "Romance", "Horror", "Historical Fiction", "Thriller"
    )

    private val _categoryBooks = mutableStateMapOf<String, List<Book>>()
    val categoryBooks: Map<String, List<Book>> get() = _categoryBooks

    fun fetchCategoryBooks(category: String) {
        viewModelScope.launch {
            val result = networkResult { api.getBooks(category) }
            if (result is UiState.Success) {
                _categoryBooks[category] = result.data.items.map { it.toBook() }
            }
        }
    }

    fun fetchAllCategories() {
        categories.forEach { category ->
            fetchCategoryBooks(category)
        }
    }

    fun addBook(book: Book) {
        viewModelScope.launch {
            booksDao.insertBook(book)
        }
    }


}
