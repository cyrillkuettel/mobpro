package com.example.android.architecture.blueprints.todoapp.statistics

import com.example.android.architecture.blueprints.todoapp.data.Task
import org.hamcrest.Matchers.`is`
import org.junit.Assert.*

import org.junit.Test

class StatisticsUtilsTest {

    @Test
    fun getActiveAndCompletedStats_noCompleted_returnsHundredZero() {

        // Create an active task
        val tasks = listOf<Task>(
            Task("title", "desc", isCompleted = false)
        )
        // Call your function
        val result = getActiveAndCompletedStats(tasks)

        // Check the result
        assertThat(result.activeTasksPercent, `is`(100f))
        assertThat(result.completedTasksPercent, `is`(0f))
    }



    @Test
    fun getActiveAndCompletedStats_oneCompletedAndNoActive_returnsZeroHundred() {

        // Create an active task
        val tasks = listOf<Task>(
            Task("title2", "desc", isCompleted = true)
        )
        // Call your function
        val result = getActiveAndCompletedStats(tasks)

        // Check the result
        assertThat(result.activeTasksPercent, `is`(0f))
        assertThat(result.completedTasksPercent, `is`(100f))
    }

    @Test
    fun getActiveAndCompletedStats_twoCompletedAndThreeActive_returnsFourtySixty() {

        // Create an active task
        val tasks = listOf<Task>(
            Task("title2", "desc", isCompleted = true),
            Task("title2", "desc", isCompleted = true),

            Task("title2", "desc", isCompleted = false),
            Task("title2", "desc", isCompleted = false),
            Task("title2", "desc", isCompleted = false),
            )

        // Call your function
        val result = getActiveAndCompletedStats(tasks)

        // Check the result
        assertThat(result.activeTasksPercent, `is`(60f))
        assertThat(result.completedTasksPercent, `is`(40f))
    }

    @Test
    fun getActiveAndCompletedStats_empty_returnsZeros() {

        // Create an active task
        val emptyTasks = emptyList<Task>()

        // Call your function
        val result = getActiveAndCompletedStats(emptyTasks)

        // Check the result
        assertThat(result.activeTasksPercent, `is`(0f))
        assertThat(result.completedTasksPercent, `is`(0f))
    }

    @Test
    fun getActiveAndCompletedStats_null_returnsZeros() {

        // Create an active task
        val errorTask = null

        // Call your function
        val result = getActiveAndCompletedStats(errorTask)

        // Check the result
        assertThat(result.activeTasksPercent, `is`(0f))
        assertThat(result.completedTasksPercent, `is`(0f))
    }








}