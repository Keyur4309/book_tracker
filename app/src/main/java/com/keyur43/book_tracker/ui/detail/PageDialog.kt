package com.keyur43.book_tracker.ui.detail

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.keyur43.book_tracker.ui.components.TimePicker
import com.keyur43.book_tracker.ui.theme.BookTrackerTheme
import com.keyur43.book_tracker.util.clickWithRipple

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun PageDialog(
    initialCurrentPage: Int,
    initialTotalPages: Int,
    modifier: Modifier = Modifier,
    onSave: (Int, Int, Int) -> Unit,
) {
    var current by remember { mutableStateOf(initialCurrentPage.toString()) }
    var total by remember { mutableStateOf(initialTotalPages.toString()) }
    var editTotal by remember { mutableStateOf(false) }
    var time by remember { mutableStateOf(0) }

    val currentErrorState: () -> Boolean = {
        val currentInt = current.toIntOrNull()
        current == "" ||
                currentInt == null ||
                currentInt > (total.toIntOrNull() ?: Int.MAX_VALUE) ||
                currentInt < 1
    }

    val totalErrorState: () -> Boolean = {
        val totalInt = total.toIntOrNull()
        total == "" || totalInt == null || totalInt < 1
    }

    Dialog(
        onDismissRequest = {
            if (editTotal) { // BackHandler doesn't work
                editTotal = false
            } else {
                onSave(initialCurrentPage, initialTotalPages, 0)
            }
        },
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
            usePlatformDefaultWidth = false,
        ),
    ) {
        Surface(
            modifier = modifier
                .padding(16.dp),
            shape = RoundedCornerShape(4.dp),
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.height(32.dp))
                PageInputField(
                    current,
                    currentErrorState,
                    label = "Current Page",
                    onChange = { current = it },
                )
                Spacer(Modifier.height(16.dp))
                AddCurrentChips {
                    current = ((current.toIntOrNull() ?: 0) + it).toString()
                }
                TextFieldDivider()
                Spacer(Modifier.height(16.dp))
                TotalPagesSection(
                    editTotal = editTotal,
                    total = total,
                    totalErrorState = totalErrorState,
                    onEditClicked = { editTotal = !editTotal },
                    onChange = { total = it },
                )
                Spacer(Modifier.height(32.dp))
                TimeInput((current.toIntOrNull() ?: -1) > initialCurrentPage) { time = it }
                AnimatedVisibility(
                    (total.toIntOrNull() != initialTotalPages || current.toIntOrNull() != initialCurrentPage)
                            &&
                            (!currentErrorState() && !totalErrorState())
                ) {
                    Button(
                        onClick = { onSave(current.toInt(), total.toInt(), time) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .height(48.dp)
                    ) {
                        Text("Save")
                    }
                }
            }
        }
    }
}

@Composable
private fun TimeInput(show: Boolean, onTimeChange: (Int) -> Unit) {
    AnimatedVisibility(show) {
        Column {
            Spacer(Modifier.height(32.dp))
            Text(
                text = "For how long did you read today?",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(start = 8.dp)
            )
            Spacer(Modifier.height(8.dp))
            TimePicker(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
            ) { hour, minute ->
                onTimeChange(hour * 60 + minute)
            }
            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
fun AddCurrentChips(modifier: Modifier = Modifier, onChipClicked: (Int) -> Unit) {
    val options = remember { listOf(1, 2, 5, 10, 15, 20, 25, 30, 50, 75, 100) }
    LazyRow(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(options) {
            AssistChip(
                onClick = { onChipClicked(it) },
                label = { Text("+$it") },
            )
        }
    }
}

@Composable
private fun TotalPagesSection(
    editTotal: Boolean,
    total: String,
    totalErrorState: () -> Boolean,
    onEditClicked: () -> Unit,
    onChange: (String) -> Unit,
) {
    AnimatedVisibility(visible = editTotal) {
        PageInputField(
            current = total,
            isError = totalErrorState,
            label = "Total Pages",
            onChange = onChange,
        )
    }
    AnimatedVisibility(visible = !editTotal) {
        Box(
            modifier = Modifier.fillMaxWidth(0.9f)
        ) {
            Text(
                text = total,
                fontSize = 32.sp,
                modifier = Modifier.align(Alignment.Center)
            )
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "Edit Total Time",
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 16.dp)
                    .clickWithRipple(onClick = onEditClicked)
            )
        }
    }
}

@Composable
private fun PageInputField(
    current: String,
    isError: () -> Boolean,
    label: String = "Current Page",
    onChange: (String) -> Unit,
) {
    OutlinedTextField(
        value = current,
        onValueChange = {
            onChange(it)
        },
        label = { Text(label) },
        textStyle = LocalTextStyle.current.copy(
            textAlign = TextAlign.Center,
            fontSize = 32.sp
        ),
        isError = isError(),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number
        )
    )
}

@Composable
private fun TextFieldDivider(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .height(3.dp)
            .background(Color.Gray)
    )
}

@Preview(showBackground = true)
@Composable
fun PageDialogPreview() {
    BookTrackerTheme {
        PageDialog(initialCurrentPage = 50, initialTotalPages = 200) { current, total, minutes ->

        }
    }
}