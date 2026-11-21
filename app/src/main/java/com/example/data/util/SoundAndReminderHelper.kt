package com.example.data.util

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.R

object SoundAndReminderHelper {
    private const val CHANNEL_ID = "footballpro_training_reminders"

    fun playCueBeep(isFinishOrWhistle: Boolean = false) {
        try {
            val toneType = if (isFinishOrWhistle) ToneGenerator.TONE_PROP_BEEP2 else ToneGenerator.TONE_PROP_BEEP
            val durationMs = if (isFinishOrWhistle) 320 else 150
            val generator = ToneGenerator(AudioManager.STREAM_MUSIC, 80)
            generator.startTone(toneType, durationMs)
        } catch (_: Exception) {
            // Safely ignore on devices/tests without audio hardware
        }
    }

    fun showTrainingReminderNotification(
        context: Context,
        title: String,
        message: String,
        notificationId: Int = 202
    ): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val granted = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
            if (!granted) return false
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Football Training Reminders",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Offline daily training session reminders"
            }
            val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification_football)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        return try {
            NotificationManagerCompat.from(context).notify(notificationId, notification)
            true
        } catch (_: SecurityException) {
            false
        }
    }
}
