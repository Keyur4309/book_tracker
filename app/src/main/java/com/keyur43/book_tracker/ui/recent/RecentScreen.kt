package com.keyur43.book_tracker.ui.recent

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.keyur43.book_tracker.R
import com.keyur43.book_tracker.data.Book
import com.keyur43.book_tracker.util.Screen


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecentScreen(navController: NavController, viewModel: BookViewModel = hiltViewModel()) {

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
                text = "Book",
                maxLines = 1,
                color = Color(0xFF000000),
                overflow = TextOverflow.Ellipsis
            )

        }
        BookCategoryList(viewModel, navController)


    }

}

@Composable
fun BookCategoryList(viewModel: BookViewModel = hiltViewModel(), navController: NavController) {
    val categoryBooks by remember { derivedStateOf { viewModel.categoryBooks } }

    LaunchedEffect(Unit) {
        viewModel.fetchAllCategories()
    }

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        viewModel.categories.forEach { category ->
            item {
                val books = categoryBooks[category].orEmpty()
                Text(
                    text = category,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(top = 15.dp, bottom = 5.dp, start = 15.dp)
                )
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(books) { book ->
                        BookSearchItem(book) {
                            viewModel.addBook(book = book)
                            navController.navigate("${Screen.BookDetail.route}/${it}")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BookSearchItem(book: Book, onBookClicked: (String) -> Unit) {

    Row(
        modifier = Modifier
            .width(135.dp)
            .height(165.dp)
            .clip(shape = RoundedCornerShape(20.dp))
            .clickable { onBookClicked(book.id) }) {
        Image(
            modifier = Modifier.fillMaxSize(), painter = rememberAsyncImagePainter(
                model = ImageRequest.Builder(LocalContext.current).data(book.thumbnail)
                    .placeholder(R.drawable.placeholder_book) // ✅ Your placeholder image in res/drawable
                    .error(R.drawable.placeholder_book) // Optional: shown on failure
                    .crossfade(true).build()
            ), contentDescription = "Book Cover", contentScale = ContentScale.Crop
        )
    }
}

