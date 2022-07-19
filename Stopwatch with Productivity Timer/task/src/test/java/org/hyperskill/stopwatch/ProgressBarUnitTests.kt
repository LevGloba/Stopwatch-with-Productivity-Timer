package org.hyperskill.stopwatch

import android.app.Activity
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import org.hyperskill.stopwatch.TestUtils.findViewByString
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows
import java.util.concurrent.TimeUnit

// Version 1.4.2
@RunWith(RobolectricTestRunner::class)
class ProgressBarUnitTests {

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
    private val progressBar: ProgressBar by lazy {
        findViewByString("progressBar", activity)
    }

    private val messageInvalidVisibility =
            "invalid progress bar visibility (VISIBLE = 0, INVISIBLE = 4, GONE = 8)"

    private val messageInvalidColor = "invalid progress bar color. LastColor: %d"


    @Test
    fun testShouldCheckProgressBarExists() {
        progressBar
    }

    @Test
    fun testShouldCheckProgressBarInvisibilityOnInit() {
        val unexpected = View.VISIBLE

        val actual = progressBar.visibility
        Assert.assertNotEquals(messageInvalidVisibility, unexpected, actual)
    }

    @Test
    fun testShouldCheckProgressBarVisibilityOnStart() {
        val expected = View.VISIBLE

        startButton.performClick()

        val actual = progressBar.visibility
        Assert.assertEquals(messageInvalidVisibility, expected, actual)
    }

    @Test
    fun testShouldCheckProgressBarInvisibilityOnReset() {
        val expectAfterStart = View.VISIBLE
        val unexpectedAfterReset = View.VISIBLE

        startButton.performClick()

        val actualAfterStart = progressBar.visibility
        Assert.assertEquals(messageInvalidVisibility, expectAfterStart, actualAfterStart)

        resetButton.performClick()

        val actualAfterReset = progressBar.visibility
        Assert.assertNotEquals(messageInvalidVisibility, unexpectedAfterReset, actualAfterReset)
    }

    @Test
    fun testShouldCheckMultipleClicks() {
        val expectedAfterStart = View.VISIBLE
        val unexpectedAfterReset = View.VISIBLE

        startButton.performClick()
        startButton.performClick()
        Thread.sleep(1100)
        Shadows.shadowOf(Looper.getMainLooper()).idleFor(1100, TimeUnit.MILLISECONDS)


        val actualAfterStart = progressBar.visibility
        Assert.assertEquals(messageInvalidVisibility, expectedAfterStart, actualAfterStart)

        resetButton.performClick()
        resetButton.performClick()
        Thread.sleep(300)
        Shadows.shadowOf(Looper.getMainLooper()).idleFor(300, TimeUnit.MILLISECONDS)


        val actualAfterReset = progressBar.visibility
        Assert.assertNotEquals(messageInvalidVisibility, unexpectedAfterReset, actualAfterReset)
    }

    @Test
    fun testShouldCheckProgressBarColorEachSecond() {

        startButton.performClick()
        Shadows.shadowOf(Looper.getMainLooper()).idleFor(100, TimeUnit.MILLISECONDS)
        Thread.sleep(100)

        var lastColor: Int? = progressBar.indeterminateTintList?.defaultColor

        for (i in 0 until 5) {
            Shadows.shadowOf(Looper.getMainLooper()).idleFor(1100, TimeUnit.MILLISECONDS)
            Thread.sleep(1100)

            val actualColor = progressBar.indeterminateTintList?.defaultColor

            if(lastColor == actualColor) {
                Shadows.shadowOf(Looper.getMainLooper()).runToEndOfTasks() // to prevent this misleading message since this in not the cause of test failure: java.lang.Exception: Main looper has queued unexecuted runnables. This might be the cause of the test failure. You might need a shadowOf(getMainLooper()).idle() call.
                Assert.fail("$messageInvalidColor\nLastColor: $lastColor\nCurrentColor: $actualColor")
            }
            //Assert.assertNotEquals("$messageInvalidColor\nLastColor: $lastColor", lastColor, actualColor)  // replaced by the if(lastColor == actualColor)

            lastColor = actualColor
        }
    }
}