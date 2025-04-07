package com.keyur43.book_tracker.ui.home

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.keyur43.book_tracker.data.Book
import com.keyur43.book_tracker.ui.components.BookRow
import com.keyur43.book_tracker.ui.components.EmptyLottie

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnrememberedMutableState")
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
    onBookClicked: (String) -> Unit
) {
    val finishedBooks by derivedStateOf { viewModel.books.filter { it.currentPage == it.pageCount } }
    val currentlyReadingBooks by derivedStateOf { viewModel.books.filter { it.currentPage > 1 && it.currentPage < it.pageCount } }
    val notStartedBooks by derivedStateOf { viewModel.books.filter { it.currentPage == 1 } }

    fun LazyListScope.bookRow(books: List<Book>) {
        items(books, key = { it.id }) {
            BookRow(
                book = it,
                isFromAddScreen = false,
                onDeleteClicked = { viewModel.removeBook(it) },
                onClick = { onBookClicked(it.id) }
            )
        }
    }


    Column {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .background(Color(0xFF80CBC4)),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                modifier = Modifier.padding(start = 15.dp),
                text = "My Books",
                color = Color(0xFF000000),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

        }

        LazyColumn(
            modifier = modifier
                .fillMaxHeight()
        ) {
            if (viewModel.books.isEmpty()) {
                item {
                    EmptyLottie()
                }
            }
            if (currentlyReadingBooks.isNotEmpty()) {
                item {
                    Text(
                        text = "Currently Reading",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth()
                    )
                }
                bookRow(currentlyReadingBooks)
            }
            if (notStartedBooks.isNotEmpty()) {
                item {
                    Text(
                        text = "Not Started Yet",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth()
                    )
                }
                bookRow(notStartedBooks)
            }
            if (finishedBooks.isNotEmpty()) {
                item {
                    Text(
                        text = "Finished",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth()
                    )
                }
                bookRow(finishedBooks)
            }
        }


    }


}
