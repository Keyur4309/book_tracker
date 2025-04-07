package com.keyur43.book_tracker.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.keyur43.book_tracker.R
import com.keyur43.book_tracker.data.Book
import com.keyur43.book_tracker.data.progress
import com.keyur43.book_tracker.ui.theme.BookTrackerTheme

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BookRow(
    book: Book,
    isFromAddScreen: Boolean = false,
    onDeleteClicked: (Book) -> Unit = { },
    onClick: () -> Unit,
) {
    var dropdownExpanded by remember { mutableStateOf(false) }
    val screenWidth = LocalConfiguration.current.screenWidthDp
    val animatedAlpha by rememberInfiniteTransition().animateFloat(
        initialValue = 0.2f, targetValue = 0.8f, animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing), repeatMode = RepeatMode.Reverse
        )
    )
    var imageLoaded by remember { mutableStateOf(false) }

    DropdownMenu(
        expanded = dropdownExpanded,
        onDismissRequest = { dropdownExpanded = false },
        offset = DpOffset(x = (0.6f * screenWidth).dp, y = (-32).dp),
    ) {
        DropdownMenuItem(text = { Text("Delete") }, { onDeleteClicked(book) })
    }
    /*
        Row(
            modifier = Modifier
                .height(IntrinsicSize.Min)
                .padding(15.dp)
                .background(Color.White, RoundedCornerShape(20.dp))
                .combinedClickable(
                    enabled = true,
                    onClick = { onClick() },
                    onLongClick = { dropdownExpanded = true }),

            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = book.thumbnail,
                contentDescription = "Book Thumbnail",
                modifier = Modifier
                    .width(105.dp)
                    .padding(vertical = 10.dp)
                    .height(140.dp)
                    .padding(start = 10.dp)
                    .clip(shape = RoundedCornerShape(20.dp)),
                placeholder = painterResource(id = R.drawable.placeholder_book),
                error = painterResource(id = R.drawable.placeholder_book),
                onError = { imageLoaded = true },
                onSuccess = { imageLoaded = true },
                alpha = if (imageLoaded) 1f else animatedAlpha,
                contentScale = ContentScale.Crop,
            )
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(16.dp)
                    .weight(1f)
            ) {
                Text(
                    text = book.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = book.authors.joinToString(", "),
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis

                )
                Spacer(Modifier.height(16.dp))
                if (!isFromAddScreen) LinearProgressIndicator(
                    progress = { book.progress },
                )
            }
        }
    */

    Box(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .height(IntrinsicSize.Min)
                .padding(15.dp)
                .background(Color.White, RoundedCornerShape(20.dp))
                .combinedClickable(
                    enabled = true,
                    onClick = { onClick() },
                    onLongClick = { dropdownExpanded = true }
                )
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = book.thumbnail,
                contentDescription = "Book Thumbnail",
                modifier = Modifier
                    .width(105.dp)
                    .padding(vertical = 10.dp)
                    .height(140.dp)
                    .padding(start = 10.dp)
                    .clip(shape = RoundedCornerShape(20.dp)),
                placeholder = painterResource(id = R.drawable.placeholder_book),
                error = painterResource(id = R.drawable.placeholder_book),
                onError = { imageLoaded = true },
                onSuccess = { imageLoaded = true },
                alpha = if (imageLoaded) 1f else animatedAlpha,
                contentScale = ContentScale.Crop,
            )

            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(16.dp)
                    .weight(1f)
            ) {
                Text(
                    text = book.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = book.authors.joinToString(", "),
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(16.dp))
                if (!isFromAddScreen) {
                    LinearProgressIndicator(progress = { book.progress })
                }
            }
        }

        IconButton(
            onClick = { onDeleteClicked(book) },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(10.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete",
                tint = MaterialTheme.colorScheme.error
            )
        }
    }


}


@Preview(showBackground = true)
@Composable
fun BookRowPreview() {
    BookTrackerTheme {
        BookRow(
            Book(
                "1",
                "A Brief History of Time",
                subtitle = "Let's find out more about the universe",
                description = "A monumental popular science book written by physicist Stephen Hawking that helps you learn how the universe works.",
                authors = listOf("Stephen Hawking"),
                publisher = "Bantam Books",
                publishedDate = "1988",
                currentPage = 1,
                pageCount = 256,
                isbn = listOf("0553380168", "9780553380163"),
                categories = listOf("Non-fiction", "Science"),
                thumbnail = "http://books.google.com/books/content?id=3OTPMeElnW0C&printsec=frontcover&img=1&zoom=1&source=gbs_api",
                averageRating = 5.0,
                ratingsCount = 1,
            )
        ) {

        }
    }
}