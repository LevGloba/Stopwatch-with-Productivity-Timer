package org.hyperskill.stopwatch

import android.app.AlertDialog
import android.app.Notification
import android.app.NotificationManager
import android.content.Context
import android.graphics.drawable.Icon
import android.os.Looper
import android.widget.Button
import android.widget.EditText
import junit.framework.TestCase.assertEquals
import org.hyperskill.stopwatch.TestUtils.findViewByString
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows
import org.robolectric.shadows.ShadowAlertDialog
import java.util.concurrent.TimeUnit

//Version 1.4
@RunWith(RobolectricTestRunner::class)
class NotificationUnitTest {

    private val activityController = Robolectric.buildActivity(MainActivity::class.java)

    private val activity by lazy {
        activityController.setup().get()
    }
    private val notificationManager by lazy {
        Shadows.shadowOf(
                activity.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        )
    }
    private val settingsButton: Button by lazy {
        findViewByString("settingsButton", activity)
    }
    private val startButton: Button by lazy {
        findViewByString("startButton", activity)
    }


    @Test
    fun testShouldCheckNotificationVisibilityOnTimeExceed() {
        val secondsToCount = 1

        settingsButton.performClick()
        val dialog = ShadowAlertDialog.getLatestAlertDialog()
        dialog.findViewByString<EditText>("upperLimitEditText").setText("$secondsToCount")
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).performClick()

        startButton.performClick()

        val timeToSleep = secondsToCount * 1000 + 1100L
        Thread.sleep(timeToSleep)
        Shadows.shadowOf(Looper.getMainLooper()).idleFor(timeToSleep, TimeUnit.MILLISECONDS)


        val notification: Notification? = notificationManager.getNotification(393939)

        val messageNotificationId =
                "Could not find notification with id 393939. Did you set the proper id?"
        assertNotNull(messageNotificationId, notification)
        notification!!

        val messageChannelId = "The notification channel id does not equals \"org.hyperskill\""
        val actualChannelId = notification.channelId
        assertEquals(messageChannelId, "org.hyperskill", actualChannelId)

        val messageIcon = "Have you set the notification smallIcon?"
        val actualIcon: Icon? = notification.smallIcon
        assertNotNull(messageIcon, actualIcon)

        val messageTitle = "Have you set the notification title?"
        val actualTitle = notification.extras.getCharSequence(Notification.EXTRA_TITLE)?.toString()
        assertNotNull(messageTitle, actualTitle)

        val messageContent = "Have you set the notification content?"
        val actualContent = notification.extras.getCharSequence(Notification.EXTRA_TEXT)?.toString()
        assertNotNull(messageContent, actualContent)
    }
}
