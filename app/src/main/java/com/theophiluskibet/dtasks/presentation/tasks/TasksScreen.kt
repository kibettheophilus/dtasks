package com.theophiluskibet.dtasks.presentation.tasks

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.theophiluskibet.dtasks.R
import com.theophiluskibet.dtasks.domain.models.TaskModel
import com.theophiluskibet.dtasks.domain.models.isDue
import com.theophiluskibet.dtasks.helpers.LocalDateTime
import com.theophiluskibet.dtasks.helpers.formatDueDate
import com.theophiluskibet.dtasks.presentation.components.DLoadingComponent
import com.theophiluskibet.dtasks.presentation.task.TaskBottomSheet
import com.theophiluskibet.dtasks.presentation.ui.theme.BackgroundGray
import com.theophiluskibet.dtasks.presentation.ui.theme.DTasksTheme
import com.theophiluskibet.dtasks.presentation.ui.theme.DeleteRed
import com.theophiluskibet.dtasks.presentation.ui.theme.FilterChipBackground
import com.theophiluskibet.dtasks.presentation.ui.theme.FilterChipSelected
import com.theophiluskibet.dtasks.presentation.ui.theme.PrimaryBlue
import com.theophiluskibet.dtasks.presentation.ui.theme.SurfaceGray
import com.theophiluskibet.dtasks.presentation.ui.theme.TextPrimary
import com.theophiluskibet.dtasks.presentation.ui.theme.TextSecondary
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import org.koin.androidx.compose.koinViewModel
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

/**
 * A composable that provides the UI for the tasks screen.
 *
 * @param modifier The modifier to apply to this composable.
 * @param viewModel The [TasksViewModel] for this screen.
 */
@Composable
fun TasksScreen(
    modifier: Modifier = Modifier,
    viewModel: TasksViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    TasksScreenContent(
        uiState = uiState,
        onTaskClick = viewModel::onTaskClick,
        onTaskComplete = viewModel::toggleTaskCompletion,
        onTaskDelete = viewModel::deleteTask,
        onTaskEdit = viewModel::editTask,
        onAddTask = viewModel::addTask,
        onSaveTask = viewModel::saveTask,
        onHideBottomSheet = viewModel::hideBottomSheet,
        onRetry = viewModel::retryLoadingTasks
    )
}

/**
 * The main content of the tasks screen.
 *
 * @param uiState The [TasksUiState] for this screen.
 * @param onTaskClick A callback to be invoked when a task is clicked.
 * @param onTaskComplete A callback to be invoked when a task is marked as complete.
 * @param onTaskDelete A callback to be invoked when a task is deleted.
 * @param onTaskEdit A callback to be invoked when a task is edited.
 * @param onAddTask A callback to be invoked when the add task button is clicked.
 * @param onSaveTask A callback to be invoked when a task is saved.
 * @param onHideBottomSheet A callback to be invoked when the bottom sheet is hidden.
 * @param onRetry A callback to be invoked when the user retries loading the tasks.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TasksScreenContent(
    uiState: TasksUiState,
    onTaskClick: (TaskModel) -> Unit = {},
    onTaskComplete: (TaskModel) -> Unit = {},
    onTaskDelete: (TaskModel) -> Unit = {},
    onTaskEdit: (TaskModel) -> Unit = {},
    onAddTask: () -> Unit = {},
    onSaveTask: (TaskModel) -> Unit = {},
    onHideBottomSheet: () -> Unit = {},
    onRetry: () -> Unit = {}
) {
    var selectedFilter by remember { mutableStateOf(TaskFilter.ALL) }

    val filteredTasks = when (selectedFilter) {
        TaskFilter.ALL -> uiState.tasks.sortedWith(compareBy<TaskModel> { it.isCompleted }.thenBy { it.dueDate })
        TaskFilter.DUE -> uiState.tasks.filter { it.isDue() }
        TaskFilter.COMPLETED -> uiState.tasks.filter { it.isCompleted }
    }

    val hasIncompleteTasks = uiState.tasks.any { !it.isCompleted }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundGray)
        ) {
            // Header
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.tasks_screen_title),
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary
                    )
                },
                actions = {
                    FloatingActionButton(
                        onClick = onAddTask,
                        modifier = Modifier
                            .padding(end = 16.dp)
                            .size(56.dp),
                        containerColor = PrimaryBlue,
                        contentColor = Color.White
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = stringResource(id = R.string.add_task_button_content_description),
                            modifier = Modifier.size(24.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )

            // Filter Chips
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(TaskFilter.entries.toTypedArray()) { filter ->
                    FilterChip(
                        onClick = { selectedFilter = filter },
                        label = {
                            Text(
                                text = filter.displayName,
                                fontSize = 14.sp,
                                fontWeight = if (selectedFilter == filter) FontWeight.Medium else FontWeight.Normal
                            )
                        },
                        selected = selectedFilter == filter,
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = FilterChipSelected,
                            selectedLabelColor = Color.White,
                            containerColor = FilterChipBackground,
                            labelColor = TextPrimary
                        ),
                        modifier = Modifier.height(40.dp)
                    )
                }
            }

            // Content
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
            ) {
                when {
                    uiState.isLoading -> {
                        // Loading State
                        DLoadingComponent()
                    }

                    uiState.errorMessage != null -> {
                        // Error State
                        ErrorContent(
                            errorMessage = uiState.errorMessage,
                            onRetry = onRetry,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }

                    hasIncompleteTasks || selectedFilter == TaskFilter.COMPLETED -> {
                        // Task List
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(horizontal = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(filteredTasks) { task ->
                                TaskItem(
                                    task = task,
                                    onTaskClick = { onTaskClick(task) },
                                    onTaskComplete = { onTaskComplete(task) },
                                    onTaskDelete = { onTaskDelete(task) },
                                    onTaskEdit = { onTaskEdit(task) }
                                )
                            }

                            item {
                                Spacer(modifier = Modifier.height(80.dp))
                            }
                        }
                    }

                    else -> {
                        // Empty State
                        AllTasksCompletedContent(
                            modifier = Modifier.align(Alignment.Center),
                            onAddTask = onAddTask
                        )
                    }
                }
            }
        }

        // Task Bottom Sheet
        TaskBottomSheet(
            isVisible = uiState.showBottomSheet,
            task = uiState.taskToEdit,
            onDismiss = onHideBottomSheet,
            onSaveTask = onSaveTask
        )
    }
}

/**
 * A composable that displays a single task item.
 *
 * @param task The task to display.
 * @param onTaskClick A callback to be invoked when the task is clicked.
 * @param onTaskComplete A callback to be invoked when the task is marked as complete.
 * @param onTaskDelete A callback to be invoked when the task is deleted.
 * @param onTaskEdit A callback to be invoked when the task is edited.
 * @param modifier The modifier to apply to this composable.
 */
@Composable
fun TaskItem(
    task: TaskModel,
    onTaskClick: () -> Unit,
    onTaskComplete: () -> Unit,
    onTaskDelete: () -> Unit,
    onTaskEdit: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onTaskClick() },
        colors = CardDefaults.cardColors(
            containerColor = if (task.isCompleted)
                SurfaceGray.copy(alpha = 0.6f)
            else
                SurfaceGray
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (task.isCompleted) 1.dp else 2.dp
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Completion Checkbox
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(
                        if (task.isCompleted) PrimaryBlue else Color.Transparent
                    )
                    .clickable { onTaskComplete() },
                contentAlignment = Alignment.Center
            ) {
                if (task.isCompleted) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = stringResource(id = R.string.task_completed_content_description),
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .clip(CircleShape)
                            .background(Color.Transparent)
                            .background(
                                Color.Gray.copy(alpha = 0.3f),
                                CircleShape
                            )
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Task Content
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                    color = if (task.isCompleted)
                        TextPrimary.copy(alpha = 0.6f)
                    else
                        TextPrimary,
                    textDecoration = if (task.isCompleted)
                        TextDecoration.LineThrough
                    else
                        TextDecoration.None
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = task.dueDate.formatDueDate(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (task.isCompleted)
                        TextSecondary.copy(alpha = 0.5f)
                    else
                        TextSecondary,
                    textDecoration = if (task.isCompleted)
                        TextDecoration.LineThrough
                    else
                        TextDecoration.None
                )
            }

            // Action Buttons
            Row {
                IconButton(
                    onClick = onTaskEdit,
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = stringResource(id = R.string.edit_task_button_content_description),
                        tint = if (task.isCompleted)
                            TextSecondary.copy(alpha = 0.5f)
                        else
                            TextSecondary,
                        modifier = Modifier.size(20.dp)
                    )
                }

                IconButton(
                    onClick = onTaskDelete,
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = stringResource(id = R.string.delete_task_button_content_description),
                        tint = if (task.isCompleted)
                            DeleteRed.copy(alpha = 0.5f)
                        else
                            DeleteRed,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

/**
 * A composable that displays a message indicating that all tasks are completed.
 *
 * @param modifier The modifier to apply to this composable.
 * @param onAddTask A callback to be invoked when the add task button is clicked.
 */
@Composable
fun AllTasksCompletedContent(
    modifier: Modifier = Modifier,
    onAddTask: () -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Success Icon
        Box(
            modifier = Modifier
                .size(120.dp)
                .background(
                    PrimaryBlue,
                    CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = stringResource(id = R.string.all_tasks_completed_icon_content_description),
                tint = Color.White,
                modifier = Modifier.size(60.dp)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = stringResource(id = R.string.all_tasks_completed_message),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.SemiBold,
            color = TextPrimary,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = stringResource(id = R.string.add_new_task_prompt),
            style = MaterialTheme.typography.bodyLarge,
            color = TextSecondary,
            textAlign = TextAlign.Center
        )
    }
}

/**
 * An enum representing the different task filters.
 *
 * @param displayName The display name of the filter.
 */
enum class TaskFilter(val displayName: String) {
    ALL(displayName = "All"),
    DUE(displayName = "Due"),
    COMPLETED(displayName = "Completed"),
}

/**
 * A composable that displays an error message.
 *
 * @param errorMessage The error message to display.
 * @param onRetry A callback to be invoked when the retry button is clicked.
 * @param modifier The modifier to apply to this composable.
 */
@Composable
fun ErrorContent(
    errorMessage: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Error Icon
        Box(
            modifier = Modifier
                .size(80.dp)
                .background(
                    DeleteRed.copy(alpha = 0.1f),
                    CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Add, // You might want to use an error icon here
                contentDescription = stringResource(id = R.string.error_icon_content_description),
                tint = DeleteRed,
                modifier = Modifier.size(40.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = stringResource(id = R.string.error_message_title),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.SemiBold,
            color = TextPrimary,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = errorMessage,
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary,
            textAlign = TextAlign.Center,
            lineHeight = 20.sp
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onRetry,
            colors = ButtonDefaults.buttonColors(
                containerColor = PrimaryBlue
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = stringResource(id = R.string.retry_button_text),
                color = Color.White,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

/**
 * A preview of the tasks screen.
 */
@OptIn(ExperimentalTime::class)
@Preview(showBackground = true)
@Composable
fun TasksScreenPreview() {
    DTasksTheme {
        val today = Clock.System.now()
        val tomorrow = Clock.System.now().plus(1, DateTimeUnit.DAY, TimeZone.currentSystemDefault())
        val futureDate =
            Clock.System.now().plus(5, DateTimeUnit.DAY, TimeZone.currentSystemDefault())

        val sampleTasks = listOf(
            TaskModel(
                id = "1",
                title = stringResource(id = R.string.sample_task_1_title),
                dueDate = today.LocalDateTime,
                isCompleted = false,
                createdAt = today.LocalDateTime,
                updatedAt = today.LocalDateTime
            ),
            TaskModel(
                id = "2",
                title = stringResource(id = R.string.sample_task_2_title),
                dueDate = tomorrow.LocalDateTime,
                isCompleted = false,
                createdAt = today.LocalDateTime,
                updatedAt = today.LocalDateTime
            ),
            TaskModel(
                id = "3",
                title = stringResource(id = R.string.sample_task_3_title),
                dueDate = futureDate.LocalDateTime,
                isCompleted = true,
                createdAt = today.LocalDateTime,
                updatedAt = today.LocalDateTime
            )
        )

        val uiState = TasksUiState(tasks = sampleTasks)
        TasksScreenContent(uiState = uiState)
    }
}

/**
 * A preview of the tasks screen in an empty state.
 */
@OptIn(ExperimentalTime::class)
@Preview(showBackground = true)
@Composable
fun TasksScreenEmptyPreview() {
    DTasksTheme {
        val uiState = TasksUiState(
            tasks = listOf(
                TaskModel(
                    id = "1",
                    title = stringResource(id = R.string.sample_completed_task_title),
                    dueDate = Clock.System.now().LocalDateTime,
                    isCompleted = true,
                    createdAt = Clock.System.now().LocalDateTime,
                    updatedAt = Clock.System.now().LocalDateTime
                )
            )
        )
        TasksScreenContent(uiState = uiState)
    }
}

/**
 * A preview of the tasks screen in a loading state.
 */
@Preview(showBackground = true)
@Composable
fun TasksScreenLoadingPreview() {
    DTasksTheme {
        val uiState = TasksUiState(
            isLoading = true
        )
        TasksScreenContent(uiState = uiState)
    }
}

/**
 * A preview of the tasks screen with an error message.
 */
@Preview(showBackground = true)
@Composable
fun TasksScreenErrorPreview() {
    DTasksTheme {
        val uiState = TasksUiState(
            errorMessage = stringResource(id = R.string.tasks_error_message)
        )
        TasksScreenContent(uiState = uiState)
    }
}
