package com.keyur43.book_tracker.ui.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.keyur43.book_tracker.data.Book
import com.keyur43.book_tracker.data.Progress
import com.keyur43.book_tracker.data.minutesReadForLastSevenDays
import com.keyur43.book_tracker.data.pagesReadForLastSevenDays
import com.keyur43.book_tracker.data.progress
import com.keyur43.book_tracker.data.publishingDetails
import com.keyur43.book_tracker.ui.charty.common.axis.AxisConfig
import com.keyur43.book_tracker.ui.charty.line.LineChart
import com.keyur43.book_tracker.ui.charty.line.model.LineData
import com.keyur43.book_tracker.util.clickWithRipple

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    bookId: String,
    modifier: Modifier = Modifier,
    viewModel: DetailViewModel = hiltViewModel(),
    onBackClick: () -> Unit = {}
) {
    LaunchedEffect(bookId) {
        viewModel.getBook(bookId)
    }

    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) PageDialog(
        initialCurrentPage = viewModel.book.currentPage,
        initialTotalPages = viewModel.book.pageCount
    ) { current, total, minutes ->
        showDialog = false
        viewModel.updateProgress(bookId, current, total, minutes)
    }


    Column {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .background(Color(0xFF80CBC4)),
            contentAlignment = Alignment.CenterStart
        ) {
            Row {

                Icon(
                    modifier = Modifier
                        .padding(start = 15.dp)
                        .clickWithRipple { onBackClick() },
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color(0xFF000000)
                )

                Text(
                    modifier = Modifier.padding(start = 15.dp),
                    text = viewModel.book.title,
                    color = Color(0xFF000000),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Header(viewModel.book)
            Spacer(Modifier.height(16.dp))
            ReadCountButton(viewModel.book) { showDialog = true }
            Spacer(Modifier.height(16.dp))
            AdditionalDetails(viewModel.book)
            Graphs(viewModel.progress)
        }


    }

}


@Composable
fun Graphs(progress: List<Progress>, modifier: Modifier = Modifier) {
    if (progress.isEmpty()) return

    Spacer(Modifier.height(16.dp))

    val pagesReadData = remember(progress) {
        progress.pagesReadForLastSevenDays().map {
            LineData(it.first, it.second.toFloat())
        }
    }

    val minutesReadData = remember(progress) {
        progress.minutesReadForLastSevenDays().map {
            LineData(it.first, it.second.toFloat())
        }
    }

    if (pagesReadData.size <= 1) {
        Text(
            "Keep reading to see your progress here!",
            modifier = modifier.padding(16.dp),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold
        )
    } else {
        Text(
            text = "Pages Read",
            textAlign = TextAlign.Center,
        )
        LineChart(
            lineData = pagesReadData,
            color = MaterialTheme.colorScheme.primary,
            modifier = modifier
                .height(250.dp)
                .fillMaxWidth()
                .padding(vertical = 32.dp, horizontal = 32.dp),
            axisConfig = AxisConfig(
                showAxis = true,
                isAxisDashed = false,
                showUnitLabels = true,
                showXLabels = true,
                xAxisColor = MaterialTheme.colorScheme.onSurface,
                yAxisColor = MaterialTheme.colorScheme.onSurface,
            )
        )
    }
    Spacer(Modifier.height(32.dp))

    if (minutesReadData.size > 1) {
        Text(
            text = "Minutes Read",
            textAlign = TextAlign.Center,
        )
        LineChart(
            lineData = minutesReadData,
            color = MaterialTheme.colorScheme.primary,
            modifier = modifier
                .height(250.dp)
                .fillMaxWidth()
                .padding(vertical = 32.dp, horizontal = 32.dp),
            axisConfig = AxisConfig(
                showAxis = true,
                isAxisDashed = false,
                showUnitLabels = true,
                showXLabels = true,
                xAxisColor = MaterialTheme.colorScheme.onSurface,
                yAxisColor = MaterialTheme.colorScheme.onSurface
            ),
        )
        Spacer(Modifier.height(32.dp))
    }
}

@Composable
private fun ReadCountButton(book: Book, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .height(90.dp)
                .padding(16.dp)
        ) {

            Text(
                text = "On page ${book.currentPage} of ${book.pageCount}",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.align(Alignment.Center)
            )

            LinearProgressIndicator(
                progress = { book.progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .align(Alignment.BottomCenter)
                    .clip(RoundedCornerShape(4.dp)),
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun Header(book: Book, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.6f),
                        MaterialTheme.colorScheme.surface
                    )
                )
            )
            .padding(bottom = 24.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            AsyncImage(
                model = book.thumbnail,
                contentDescription = "Book cover",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .height(250.dp)
                    .fillMaxWidth()
                    .padding(16.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .shadow(8.dp)
            )
            Text(
                text = book.title,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Text(
                text = book.authors.joinToString(", "),
                style = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                textAlign = TextAlign.Center
            )
            if (book.categories.isNotEmpty()) {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(book.categories) {
                        if (it.isNotEmpty()) {
                            Box(
                                modifier = Modifier
                                    .background(
                                        color = MaterialTheme.colorScheme.secondary,
                                        shape = RoundedCornerShape(50)
                                    )
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = it,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSecondary
                                )
                            }
                        }
                    }
                }
            }
            if (book.ratingsCount > 0) {
                Text(
                    "⭐ ${book.averageRating} • ${book.ratingsCount} ratings",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }
    }
}

@Composable
fun AdditionalDetails(book: Book, modifier: Modifier = Modifier) {
    Column(
        modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Additional Details", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.weight(1f))
        }

        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            SelectionContainer {
                Column {
                    if (book.subtitle.isNotEmpty()) {
                        Text(text = book.subtitle, fontWeight = FontWeight.SemiBold)
                        Spacer(Modifier.height(8.dp))
                    }

                    if (book.description.isNotEmpty()) {
                        Text(text = book.description)
                        Spacer(Modifier.height(8.dp))
                    }

                    if (book.publishingDetails.isNotEmpty()) {
                        Text("Publisher: ${book.publishingDetails}")
                        Spacer(Modifier.height(8.dp))
                    }

                    if (book.isbn.isNotEmpty()) {
                        Text("ISBNs: ${book.isbn.joinToString(", ")}")
                    }
                }
            }
        }
    }
}
