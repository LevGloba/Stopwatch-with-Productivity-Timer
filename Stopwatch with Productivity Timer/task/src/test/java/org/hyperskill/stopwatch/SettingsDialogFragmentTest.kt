package org.hyperskill.stopwatch

import android.app.AlertDialog
import android.graphics.Color
import android.os.Looper
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows
import org.robolectric.shadows.ShadowAlertDialog

import org.hyperskill.stopwatch.TestUtils.findViewByString
import java.util.concurrent.TimeUnit

// version 1.4
@RunWith(RobolectricTestRunner::class)
class SettingsDialogFragmentTest {

    private val activityController = Robolectric.buildActivity(MainActivity::class.java)

    private val activity by lazy {
        activityController.setup().get()
    }

    private val settingsButton: Button by lazy {
        findViewByString("settingsButton", activity)
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

    private val messageDialogNotFound = "Is dialog shown when \"settingsButton\" is clicked?"


    @Test
    fun testShouldCheckSettingsButtonExist() {
        settingsButton
    }

    @Test
    fun testShouldCheckSettingsButtonText() {
        val expected = "Settings"
        val message = "in button property \"text\""

        val actual = settingsButton.text
        Assert.assertEquals(message, expected, actual)
    }

    @Test
    fun testShouldCheckSettingsButtonEnable() {
        val message1 = "view with id \"settingsButton\" should be enabled when timer is stopped"
        Assert.assertTrue(message1, settingsButton.isEnabled)

        startButton.performClick()

        Thread.sleep(300)
        Shadows.shadowOf(Looper.getMainLooper()).idleFor(300, TimeUnit.MILLISECONDS)

        val message2 = "view with id \"settingsButton\" should be disabled when timer is running"
        Assert.assertFalse(message2, settingsButton.isEnabled)

        resetButton.performClick()
        Assert.assertTrue(message1, settingsButton.isEnabled)
    }

    @Test
    fun testShouldShowAlertDialogOnSettingsButtonClick() {
        settingsButton.performClick()
        val dialog = ShadowAlertDialog.getLatestAlertDialog()
        Assert.assertNotNull(messageDialogNotFound, dialog)
    }

    @Test
    fun testDialogButtonsShouldContainText() {
        val expectedOk = "OK"
        val expectedCancel = "Cancel"

        settingsButton.performClick()
        val dialog = ShadowAlertDialog.getLatestAlertDialog()
        Assert.assertNotNull(messageDialogNotFound, dialog)

        val actualOk = dialog.getButton(AlertDialog.BUTTON_POSITIVE).text
        Assert.assertEquals("positive button contains wrong text", expectedOk, actualOk)

        val actualCancel = dialog.getButton(AlertDialog.BUTTON_NEGATIVE).text
        Assert.assertEquals("negative button contains wrong text", expectedCancel, actualCancel)
    }

    @Test
    fun testShouldCheckLimitIsNotSetOnCancel() {
        val secondsToCount = 2

        settingsButton.performClick()
        val dialog = ShadowAlertDialog.getLatestAlertDialog()
        Assert.assertNotNull(messageDialogNotFound, dialog)

        val upperLimitEditText = dialog.findViewByString<EditText>("upperLimitEditText")
        upperLimitEditText.setText("$secondsToCount")

        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).performClick()

        startButton.performClick()

        val sleepTime = secondsToCount * 1000 + 1300L
        Thread.sleep(sleepTime)
        Shadows.shadowOf(Looper.getMainLooper()).idleFor(sleepTime, TimeUnit.MILLISECONDS)

        val message = "\"textView\" color should not be RED if limit has not been set"
        val actual = textView.currentTextColor
        Assert.assertNotEquals(message, Color.RED, actual)
    }

    @Test
    fun testShouldCheckLimitIsSetOnOk() {
        val secondsToCount = 2L

        settingsButton.performClick()
        val dialog = ShadowAlertDialog.getLatestAlertDialog()
        Assert.assertNotNull(messageDialogNotFound, dialog)

        dialog.findViewByString<EditText>("upperLimitEditText").setText("$secondsToCount")
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).performClick()

        startButton.performClick()
        Thread.sleep(300L)
        Shadows.shadowOf(Looper.getMainLooper()).idleFor(300L, TimeUnit.MILLISECONDS)

        val messageBefore = "\"textView\" color should not be RED before limit is reached"
        val actualBefore = textView.currentTextColor
        Assert.assertNotEquals(messageBefore, Color.RED, actualBefore)

        val sleepTime = secondsToCount * 1000L + 1000L
        Thread.sleep(sleepTime)
        Shadows.shadowOf(Looper.getMainLooper()).idleFor(sleepTime, TimeUnit.MILLISECONDS)

        val messageAfter = "\"textView\" color should be RED after limit is reached"
        val actualAfter = textView.currentTextColor
        Assert.assertEquals(messageAfter, Color.RED, actualAfter)
    }

    @Test
    fun testShouldCheckColorsOnRestart() {
        val secondsToCount = 2L

        settingsButton.performClick()
        val dialog = ShadowAlertDialog.getLatestAlertDialog()
        Assert.assertNotNull(messageDialogNotFound, dialog)

        dialog.findViewByString<EditText>("upperLimitEditText").setText("$secondsToCount")
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).performClick()

        startButton.performClick()

        val timeToSleep = secondsToCount * 1000L + 1300L
        Thread.sleep(timeToSleep)
        Shadows.shadowOf(Looper.getMainLooper()).idleFor(timeToSleep, TimeUnit.MILLISECONDS)

        val messageAfter = "\"textView\" color should be RED after limit is reached"
        val actualAfter = textView.currentTextColor
        Assert.assertEquals(messageAfter, Color.RED, actualAfter)

        resetButton.performClick()
        startButton.performClick()

        Thread.sleep(300L)
        Shadows.shadowOf(Looper.getMainLooper()).idleFor(300, TimeUnit.MILLISECONDS)

        val messageBefore = "\"textView\" color should not be RED before limit is reached"
        val actualBefore = textView.currentTextColor
        Assert.assertNotEquals(messageBefore, Color.RED, actualBefore)
    }
}