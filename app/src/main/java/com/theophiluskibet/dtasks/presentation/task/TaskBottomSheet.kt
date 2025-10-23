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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.theophiluskibet.dtasks.R
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

/**
 * A bottom sheet for creating or editing a task.
 *
 * @param isVisible Whether the bottom sheet is visible.
 * @param task The task to edit, or `null` if creating a new task.
 * @param onDismiss A callback to be invoked when the bottom sheet is dismissed.
 * @param onSaveTask A callback to be invoked when the task is saved.
 * @param modifier The modifier to apply to this composable.
 */
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
                    title = if (isEditing) stringResource(id = R.string.task_bottom_sheet_edit_title) else stringResource(
                        id = R.string.task_bottom_sheet_new_title
                    ),
                    onClose = onDismiss
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Title Field
                TaskTextField(
                    label = stringResource(id = R.string.task_bottom_sheet_title_label),
                    value = title,
                    onValueChange = { title = it },
                    placeholder = stringResource(id = R.string.task_bottom_sheet_title_placeholder),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Description Field
                TaskDescriptionField(
                    value = description,
                    onValueChange = { description = it },
                    placeholder = stringResource(id = R.string.task_bottom_sheet_description_placeholder)
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
                    text = stringResource(id = R.string.task_bottom_sheet_save_button)
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

/**
 * The header for the task bottom sheet.
 *
 * @param title The title of the bottom sheet.
 * @param onClose A callback to be invoked when the close button is clicked.
 * @param modifier The modifier to apply to this composable.
 */
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
                contentDescription = stringResource(id = R.string.close_button_content_description),
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

/**
 * A text field for entering the task title.
 *
 * @param label The label for the text field.
 * @param value The current value of the text field.
 * @param onValueChange A callback to be invoked when the value of the text field changes.
 * @param placeholder The placeholder text to display when the text field is empty.
 * @param singleLine Whether the text field should be a single line.
 * @param modifier The modifier to apply to this composable.
 */
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

/**
 * A text field for entering the task description.
 *
 * @param value The current value of the text field.
 * @param onValueChange A callback to be invoked when the value of the text field changes.
 * @param placeholder The placeholder text to display when the text field is empty.
 * @param modifier The modifier to apply to this composable.
 */
@Composable
private fun TaskDescriptionField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = stringResource(id = R.string.task_bottom_sheet_description_label),
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

/**
 * A section for selecting the task due date.
 *
 * @param selectedDate The currently selected date.
 * @param onDateClick A callback to be invoked when the date section is clicked.
 * @param modifier The modifier to apply to this composable.
 */
@Composable
private fun TaskDateSection(
    selectedDate: LocalDate?,
    onDateClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = stringResource(id = R.string.task_bottom_sheet_due_date_label),
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
                    } ?: stringResource(id = R.string.task_bottom_sheet_select_date_text),
                    color = if (selectedDate != null) TextPrimary else TextSecondary,
                    fontSize = 16.sp
                )

                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = stringResource(id = R.string.task_bottom_sheet_select_date_content_description),
                    tint = TextSecondary,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

/**
 * A dialog for selecting a date.
 *
 * @param initialDate The initially selected date.
 * @param onDateSelected A callback to be invoked when a date is selected.
 * @param onDismiss A callback to be invoked when the dialog is dismissed.
 */
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
                Text(stringResource(id = R.string.dialog_ok_button), color = PrimaryBlue)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(id = R.string.dialog_cancel_button), color = TextSecondary)
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

/**
 * A preview of the task bottom sheet.
 */
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

/**
 * A preview of the task bottom sheet in edit mode.
 */
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
