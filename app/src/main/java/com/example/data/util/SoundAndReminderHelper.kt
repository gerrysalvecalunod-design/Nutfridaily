package com.example.data.util

import android.content.Context
import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager

object SoundAndReminderHelper {
    val soundStyleNames = listOf("Crisp Beep", "Pitch Whistle", "Soft Chime")
    val reminderTimeOptions = listOf("06:30", "07:00", "16:30", "18:00", "19:30")

    fun playCueBeep(isFinishOrRestEnd: Boolean = false, toneIndex: Int = 0) {
        try {
            val toneType = when (toneIndex) {
                1 -> if (isFinishOrRestEnd) ToneGenerator.TONE_SUP_CONFIRM else ToneGenerator.TONE_SUP_PIP
                2 -> if (isFinishOrRestEnd) ToneGenerator.TONE_PROP_ACK else ToneGenerator.TONE_PROP_PROMPT
                else -> if (isFinishOrRestEnd) ToneGenerator.TONE_PROP_BEEP2 else ToneGenerator.TONE_PROP_BEEP
            }
            val durationMs = if (isFinishOrRestEnd) 320 else 160
            val generator = ToneGenerator(AudioManager.STREAM_MUSIC, 85)
            generator.startTone(toneType, durationMs)
        } catch (_: Exception) {
            // Safely ignore in unit tests or headless environments without audio hardware
        }
    }

    @Suppress("DEPRECATION")
    fun triggerVibration(context: Context, isFinishOrRestEnd: Boolean = false) {
        try {
            val durationMs = if (isFinishOrRestEnd) 280L else 120L
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val manager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
                manager?.defaultVibrator?.vibrate(
                    VibrationEffect.createOneShot(durationMs, VibrationEffect.DEFAULT_AMPLITUDE)
                )
            } else {
                val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator?.vibrate(
                        VibrationEffect.createOneShot(durationMs, VibrationEffect.DEFAULT_AMPLITUDE)
                    )
                } else {
                    vibrator?.vibrate(durationMs)
                }
            }
        } catch (_: Exception) {
            // Safely ignore on devices/tests without vibrator hardware
        }
    }
}
