package com.am10.spanishwords

import android.content.Context

class FlashCardConfigRepository(context: Context) {
    private val speedList = listOf(0.5, 1.0, 1.5, 2.0, 2.5, 3.0, 3.5, 4.0, 4.5, 5.0)
    val speed: Double
        get() = speedList[speedIndex]

    var speedIndex: Int
        get() = pref.getInt("speed_index", 3)
        set(value) {
            pref.edit().putInt("speed_index", value).apply()
        }

    var isPronunciationShown: Boolean
        get() = pref.getBoolean("pronunciation", true)
        set(value) {
            pref.edit().putBoolean("pronunciation", value).apply()
        }
    private val pref = context.getSharedPreferences("flash_card", Context.MODE_PRIVATE)
}