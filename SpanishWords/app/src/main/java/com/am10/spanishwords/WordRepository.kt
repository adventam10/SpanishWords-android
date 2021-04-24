package com.am10.spanishwords

import android.content.Context
import androidx.room.Room

class WordRepository(context: Context) {
    private val database = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        "word_database"
    ).build()

    private val wordDao = database.wordDao()

    suspend fun exists() = wordDao.count() > 0
    suspend fun insertWords(words: List<Word>) {
        val list = words.toTypedArray()
        wordDao.insert(*list)
    }
    suspend fun fetchAll() = wordDao.getAll()
    suspend fun findByWordType(wordType: Int) = wordDao.findByWordType(wordType)
    suspend fun updateIsChecked(isChecked: Boolean, word: Word): Word {
        val newWord = Word(
                word.id, word.spanish,
                word.pronunciation, word.japanese,
                word.wordType, isChecked
        )
        wordDao.update(newWord)
        return newWord
    }
}


