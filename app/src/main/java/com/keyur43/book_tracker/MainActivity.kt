package com.keyur43.book_tracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.keyur43.book_tracker.ui.theme.Book_trackerTheme
import com.keyur43.book_tracker.view.AppNavigation

@ExperimentalMaterial3Api
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Book_trackerTheme {
                AppNavigation()
                var selectedTab by remember { mutableStateOf(0) } // Track selected tab

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopAppBar(
                            title = {
                                Text("Bookly")
                            },
                            actions = {
                                IconButton(onClick = { /* Search button action */ }) {
                                    Icon(Icons.Default.Search, contentDescription = "Search")
                                }
                            },
                            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF87CEEB)) // Sky blue color
                        )
                    },
                    bottomBar = {
                        BottomBar(selectedTab) { selectedTab = it } // Add BottomBar here
                    }
                ) { innerPadding ->
                    LazyColumn(modifier = Modifier.padding(innerPadding)) {
                        item { BookGrid() }
                    }
                }
            }
        }
    }
}

@Composable
fun BottomBar(selectedTab: Int, onTabSelected: (Int) -> Unit) {
    NavigationBar(containerColor = Color.White) {
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Home, contentDescription = "Home") },
            label = { Text("Home") },
            selected = selectedTab == 0,
            onClick = { onTabSelected(0) }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Menu, contentDescription = "Library") },
            label = { Text("Library") },
            selected = selectedTab == 1,
            onClick = { onTabSelected(1) }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Person, contentDescription = "Profile") },
            label = { Text("Profile") },
            selected = selectedTab == 2,
            onClick = { onTabSelected(2) }
        )
    }
}

// Define a data class for book categories
data class BookCategory(val categoryName: String, val bookImages: List<Int>)

@Composable
fun BookGrid(modifier: Modifier = Modifier) {
    // Define categories and their associated books
    val categories = listOf(
        BookCategory("Fiction", listOf(
            R.drawable.book_cover_1,
            R.drawable.book_cover_2,
            R.drawable.book_cover_3
        )),
        BookCategory("Non-Fiction", listOf(
            R.drawable.book_cover_4,
            R.drawable.book_cover_5,
            R.drawable.book_cover_6
        )),
        BookCategory("Science", listOf(
            R.drawable.book_cover_7,
            R.drawable.book_cover_8,
            R.drawable.book_cover_9
        )),
        BookCategory("History", listOf(
            R.drawable.book_cover_10,
            R.drawable.book_cover_11,
            R.drawable.book_cover_12
        ))
    )

    // Iterate over each category and display the category name followed by the book images
    Column(modifier = modifier.padding(8.dp)) {
        categories.forEach { category ->
            Text(
                text = category.categoryName,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 4.dp)
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier.fillMaxWidth().height(200.dp) // Enables scrolling
            ) {
                items(category.bookImages) { bookImage ->
                    BookImage(bookImage = bookImage)
                }
            }
        }
    }
}

@Composable
fun BookImage(bookImage: Int, modifier: Modifier = Modifier) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .padding(10.dp)
            .size(150.dp) // Adjust the size as per your need
    ) {
        Image(painter = painterResource(id = bookImage), contentDescription = "Book Cover")
    }
}

@Preview(showBackground = true)
@Composable
fun BookGridPreview() {
    Book_trackerTheme {
//       BookGrid()
        AppNavigation()
    }
}