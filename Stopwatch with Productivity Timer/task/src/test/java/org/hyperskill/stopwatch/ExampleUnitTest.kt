package org.hyperskill.stopwatch

import android.app.Activity
import android.widget.Button
import android.widget.TextView
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner

import org.hyperskill.stopwatch.TestUtils.findViewByString

// Version 1.4
@RunWith(RobolectricTestRunner::class)
class ExampleUnitTest {

    private val activityController = Robolectric.buildActivity(MainActivity::class.java)

    private val activity: Activity by lazy {
        activityController.setup().get()
    }
    private val startButton: Button by lazy {
        findViewByString("startButton", activity)
    }
    private val resetButton: Button by lazy {
        findViewByString("resetButton", activity)
    }
    private val textView: TextView by lazy {
        findViewByString("textView", activity)
    }


    @Test
    fun testShouldCheckStartButtonExist() {
        startButton
    }

    @Test
    fun testShouldCheckResetButtonExist() {
        resetButton
    }

    @Test
    fun testShouldCheckTextViewExist() {
        textView
    }

    @Test
    fun testShouldCheckStartButtonText() {
        val message = "in button property \"text\""
        assertEquals(message, "Start", startButton.text)
    }

    @Test
    fun testShouldCheckResetButtonText() {
        val message = "in button property \"text\""
        assertEquals(message, "Reset", resetButton.text)
    }
}