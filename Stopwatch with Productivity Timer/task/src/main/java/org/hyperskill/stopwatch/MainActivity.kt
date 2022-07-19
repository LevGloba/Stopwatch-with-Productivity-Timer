package org.hyperskill.stopwatch

import android.app.*
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat

const val CHANNEL_ID = "org.hyperskill"

class MainActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var seconds = 0
        var minutes = 0
        var limitTime = 0
        val colors = arrayOf(Color.RED, Color.GREEN, Color.BLUE)
        var color = colors[0]
        val handler = Handler(Looper.getMainLooper())

        val textView: TextView = findViewById(R.id.textView)
        val defaultTextColor =  textView.textColors
        val startButton: Button = findViewById(R.id.startButton)
        val resetButton: Button = findViewById(R.id.resetButton)
        val settingsButton: Button = findViewById(R.id.settingsButton)
        val progressBar: ProgressBar = findViewById(R.id.progressBar)

        progressBar.visibility = View.GONE

        val updateTime: Runnable = object : Runnable {
            @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
            override fun run() {
                seconds++
                when {
                    seconds == 60 -> {
                        minutes++
                        seconds = 0
                    }
                    minutes == 60 -> {
                        minutes = 0
                    }
                }

                if (limitTime != 0 && limitTime < minutes * 60 + seconds) {
                    textView.setTextColor(Color.RED)
                    notification()
                }
                color = colors[(colors.indexOf(color) + 1) % colors.size]
                progressBar.indeterminateTintList = ColorStateList.valueOf(color)
                textView.text = String.format("%02d:%02d", minutes, seconds)
                handler.postDelayed(this, 1000)

            }
        }

        startButton.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            settingsButton.isEnabled = false
            if (!handler.hasCallbacks(updateTime)) {
                handler.postDelayed(updateTime, 1000)
            }
        }

        resetButton.setOnClickListener {
            textView.setTextColor(defaultTextColor)
            progressBar.visibility = View.GONE
            settingsButton.isEnabled = true
            minutes = 0
            seconds = 0
            limitTime = 0
            textView.text = String.format("%02d:%02d", minutes, seconds)
            handler.removeCallbacks(updateTime)
        }

        settingsButton.setOnClickListener {
            val contentView = LayoutInflater.from(this).inflate(R.layout.edit_text, null, false)
            AlertDialog.Builder(this)
                .setTitle("Alert Dialog with Custom View!")
                .setView(contentView)
                .setPositiveButton(android.R.string.ok) { _, _ ->
                    val editText = contentView.findViewById<EditText>(R.id.upperLimitEditText)
                    limitTime = editText.text.toString().toInt()
                    //Handler(Looper.getMainLooper()).postDelayed({
                    //    notification()
                   // }, limitTime.toLong() + 1000)
                }
                .setNegativeButton(android.R.string.cancel) { _, _ ->
                }
                .show()
        }

    }

    private fun notification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "org.hyperskill"
            val descriptionText = "Your time out"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                    getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        val intent = Intent(applicationContext, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, 0)


       Notification.DEFAULT_ALL

        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_baseline_watch_later_24)
                .setContentTitle("Stopwatch")
                .setContentText("Time out")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setOnlyAlertOnce(true)
                .setAutoCancel(true)
                .addAction(R.drawable.ic_baseline_access_time_24, "Enter", pendingIntent)
                .addAction(R.drawable.ic_baseline_refresh_24, "Reset", pendingIntent)

        val notificationManager = this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(393939, notificationBuilder.build())
    }
}

