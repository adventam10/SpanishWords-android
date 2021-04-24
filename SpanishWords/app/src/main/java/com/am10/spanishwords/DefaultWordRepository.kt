package com.am10.spanishwords

import android.content.Context
import java.io.BufferedReader
import java.io.InputStreamReader

class DefaultWordRepository(val context: Context) {
    fun loadDefaultWords(): List<Word> {
        val inputStream = context.resources.openRawResource(R.raw.default_words)
        val reader = BufferedReader(InputStreamReader(inputStream))
        val words = mutableListOf<Word>()
        do {
            val line = reader.readLine() ?: break
            words.add(makeWordFromCSVLine(line))
        } while (true)
        reader.close()
        inputStream.close()
        return words
    }

    private fun makeWordFromCSVLine(line: String): Word = line.split(",").let {
        Word(it[0], it[1], it[2], it[3], it[4].toInt(), it[5].toInt() == 1)
    }
}