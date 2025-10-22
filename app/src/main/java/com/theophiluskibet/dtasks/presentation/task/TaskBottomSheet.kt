package com.theophiluskibet.dtasks.presentation.task

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.theophiluskibet.dtasks.domain.models.TaskModel
import com.theophiluskibet.dtasks.helpers.LocalDateTime
import com.theophiluskibet.dtasks.helpers.asEpochMilliseconds
import com.theophiluskibet.dtasks.helpers.asLocalDateTime
import com.theophiluskibet.dtasks.presentation.components.DTaskButton
import com.theophiluskibet.dtasks.presentation.ui.theme.BackgroundGray
import com.theophiluskibet.dtasks.presentation.ui.theme.DTasksTheme
import com.theophiluskibet.dtasks.presentation.ui.theme.PrimaryBlue
import com.theophiluskibet.dtasks.presentation.ui.theme.TextPrimary
import com.theophiluskibet.dtasks.presentation.ui.theme.TextSecondary
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import java.util.*
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalMaterial3Api::class, ExperimentalTime::class)
@Composable
fun TaskBottomSheet(
    isVisible: Boolean,
    task: TaskModel? = null,
    onDismiss: () -> Unit,
    onSaveTask: (TaskModel) -> Unit,
    modifier: Modifier = Modifier
) {
    var title by remember(task) { mutableStateOf(task?.title ?: "") }
    var description by remember(task) { mutableStateOf(task?.description ?: "") }
    var selectedDate by remember(task) { mutableStateOf(task?.dueDate) }
    var showDatePicker by remember { mutableStateOf(false) }

    val isEditing = task != null
    val canSave = title.isNotBlank()

    if (isVisible) {
        ModalBottomSheet(
            onDismissRequest = onDismiss,
            modifier = modifier,
            containerColor = Color.White,
            contentColor = TextPrimary,
            dragHandle = null
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .padding(top = 16.dp, bottom = 32.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Header
                TaskBottomSheetHeader(
                    title = if (isEditing) "Edit Task" else "New Task",
                    onClose = onDismiss
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Title Field
                TaskTextField(
                    label = "Title",
                    value = title,
                    onValueChange = { title = it },
                    placeholder = "e.g., Buy groceries",
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Description Field
                TaskDescriptionField(
                    value = description,
                    onValueChange = { description = it },
                    placeholder = "e.g., Milk, eggs, bread"
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Due Date Section
                TaskDateSection(
                    selectedDate = selectedDate?.date,
                    onDateClick = { showDatePicker = true }
                )

                Spacer(modifier = Modifier.height(48.dp))

                // Save Button
                DTaskButton(
                    onClick = {
                        val newTask = TaskModel(
                            id = task?.id ?: UUID.randomUUID().toString(),
                            title = title.trim(),
                            description = description.trim(),
                            dueDate = selectedDate,
                            isCompleted = task?.isCompleted ?: false,
                            createdAt = task?.createdAt ?: Clock.System.now().LocalDateTime,
                            updatedAt = kotlin.time.Clock.System.now().LocalDateTime
                        )
                        onSaveTask(newTask)
                        onDismiss()
                    },
                    enabled = canSave,
                    text = "Save Task"
                )
            }
        }
    }

    // Date Picker Dialog
    if (showDatePicker) {
        TaskDatePickerDialog(
            initialDate = selectedDate,
            onDateSelected = { date ->
                selectedDate = date
                showDatePicker = false
            },
            onDismiss = { showDatePicker = false }
        )
    }
}

@Composable
private fun TaskBottomSheetHeader(
    title: String,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onClose,
            modifier = Modifier.size(24.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Close",
                tint = TextPrimary,
                modifier = Modifier.size(20.dp)
            )
        }

        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.SemiBold,
            color = TextPrimary
        )

        // Empty space for centering
        Spacer(modifier = Modifier.size(24.dp))
    }
}

@Composable
private fun TaskTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    singleLine: Boolean = false,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium,
            color = TextPrimary,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = BackgroundGray,
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(16.dp),
            textStyle = TextStyle(
                fontSize = 16.sp,
                color = TextPrimary
            ),
            singleLine = singleLine,
            keyboardOptions = KeyboardOptions(
                imeAction = if (singleLine) ImeAction.Next else ImeAction.Default
            ),
            decorationBox = { innerTextField ->
                if (value.isEmpty()) {
                    Text(
                        text = placeholder,
                        color = TextSecondary,
                        fontSize = 16.sp
                    )
                }
                innerTextField()
            }
        )
    }
}

@Composable
private fun TaskDescriptionField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "Description",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium,
            color = TextPrimary,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .background(
                    color = BackgroundGray,
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(16.dp),
            textStyle = TextStyle(
                fontSize = 16.sp,
                color = TextPrimary,
                lineHeight = 24.sp
            ),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Default
            ),
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.TopStart
                ) {
                    if (value.isEmpty()) {
                        Text(
                            text = placeholder,
                            color = TextSecondary,
                            fontSize = 16.sp,
                            lineHeight = 24.sp
                        )
                    }
                    innerTextField()
                }
            }
        )
    }
}

@Composable
private fun TaskDateSection(
    selectedDate: LocalDate?,
    onDateClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "Due Date",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium,
            color = TextPrimary,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        Surface(
            onClick = onDateClick,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            color = BackgroundGray
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = selectedDate?.let {
                        val month =
                            it.month.name.lowercase().replaceFirstChar { c -> c.uppercase() }
                        "$month ${it.dayOfMonth}, ${it.year}"
                    } ?: "Select a date",
                    color = if (selectedDate != null) TextPrimary else TextSecondary,
                    fontSize = 16.sp
                )

                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = "Select Date",
                    tint = TextSecondary,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalTime::class)
@Composable
private fun TaskDatePickerDialog(
    initialDate: LocalDateTime?,
    onDateSelected: (LocalDateTime) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = initialDate?.asEpochMilliseconds()
            ?: Clock.System.now().LocalDateTime.asEpochMilliseconds()
    )

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        onDateSelected(millis.asLocalDateTime())
                    }
                }
            ) {
                Text("OK", color = PrimaryBlue)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = TextSecondary)
            }
        }
    ) {
        DatePicker(
            state = datePickerState,
            colors = DatePickerDefaults.colors(
                selectedDayContainerColor = PrimaryBlue,
                selectedDayContentColor = Color.White,
                todayContentColor = PrimaryBlue,
                todayDateBorderColor = PrimaryBlue
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TaskBottomSheetPreview() {
    DTasksTheme {
        TaskBottomSheet(
            isVisible = true,
            onDismiss = {},
            onSaveTask = {}
        )
    }
}

@OptIn(ExperimentalTime::class)
@Preview(showBackground = true)
@Composable
fun TaskBottomSheetEditPreview() {
    DTasksTheme {
        val date = Clock.System.now().LocalDateTime
        val sampleTask = TaskModel(
            id = "1",
            title = "Buy groceries",
            description = "Milk, eggs, bread",
            dueDate = date,
            isCompleted = false,
            createdAt = date,
            updatedAt = date
        )

        TaskBottomSheet(
            isVisible = true,
            task = sampleTask,
            onDismiss = {},
            onSaveTask = {}
        )
    }
}