package com.example.data.util

import android.media.AudioManager
import android.media.ToneGenerator

object SoundAndReminderHelper {
    val soundStyleNames = listOf("Crisp Beep", "Court Whistle", "Soft Chime")

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
}
