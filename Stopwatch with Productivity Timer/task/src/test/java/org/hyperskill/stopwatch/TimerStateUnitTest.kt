package org.hyperskill.stopwatch

import android.app.Activity
import android.os.Looper.getMainLooper
import android.widget.Button
import android.widget.TextView
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf

import org.hyperskill.stopwatch.TestUtils.findViewByString
import java.util.concurrent.TimeUnit

// Version 1.4
@RunWith(RobolectricTestRunner::class)
class TimerStateUnitTest {

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

    private val messageTextViewAssertionError = "in TextView property \"text\""

    @Test
    fun testShouldCheckTimerInitialValue() {
        val expected = "00:00"

        val actual = textView.text
        assertEquals(messageTextViewAssertionError, expected, actual)
    }

    @Test
    fun testShouldTakeOneSecondToCountOneSecondOnStartButtonClick() {
        val expected = "00:00"

        startButton.performClick()
        Thread.sleep(300)
        shadowOf(getMainLooper()).idleFor(300, TimeUnit.MILLISECONDS)

        val actual = textView.text
        assertEquals(messageTextViewAssertionError, expected, actual)
    }

    @Test
    fun testShouldCountOneSecondAfterOneSecondOnStartButtonClick() {
        val expected = "00:01"

        startButton.performClick()
        Thread.sleep(1100)
        shadowOf(getMainLooper()).idleFor(1100, TimeUnit.MILLISECONDS)

        val actual = textView.text
        assertEquals(messageTextViewAssertionError, expected, actual)
    }


    @Test
    fun testShouldStopTimerAndResetCountOnResetButtonClick() {
        val expected = "00:00"

        startButton.performClick()
        Thread.sleep(1100)
        shadowOf(getMainLooper()).idleFor(1100, TimeUnit.MILLISECONDS)

        resetButton.performClick()
        Thread.sleep(200)
        shadowOf(getMainLooper()).idleFor(200, TimeUnit.MILLISECONDS)

        val actual = textView.text
        assertEquals(messageTextViewAssertionError, expected, actual)
    }

    @Test
    fun testShouldContinueCountOnPressingStartButtonAgain() {
        val expected = "00:02"
        startButton.performClick()
        Thread.sleep(1100)
        shadowOf(getMainLooper()).idleFor(1100, TimeUnit.MILLISECONDS)

        startButton.performClick()
        startButton.performClick()
        startButton.performClick()
        Thread.sleep(1100)
        shadowOf(getMainLooper()).idleFor(1100, TimeUnit.MILLISECONDS)

        val actual = textView.text
        assertEquals(messageTextViewAssertionError, expected, actual)
    }

    @Test
    fun testShouldIgnorePressingResetButtonAgain() {
        val expected = "00:00"

        startButton.performClick()
        Thread.sleep(1100)
        shadowOf(getMainLooper()).idleFor(1100, TimeUnit.MILLISECONDS)

        resetButton.performClick()
        Thread.sleep(200)
        shadowOf(getMainLooper()).idleFor(200, TimeUnit.MILLISECONDS)

        resetButton.performClick()
        Thread.sleep(200)
        shadowOf(getMainLooper()).idleFor(200, TimeUnit.MILLISECONDS)

        val actual = textView.text
        assertEquals(messageTextViewAssertionError, expected, actual)
    }
}