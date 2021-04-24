package com.am10.spanishwords

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface WordDao {
    @Query("SELECT * FROM word")
    suspend fun getAll(): List<Word>

    @Query("SELECT COUNT(*) FROM word")
    suspend fun count(): Int

    @Insert
    suspend fun insert(vararg words: Word)

    @Update
    suspend fun update(vararg words: Word)

    @Query("SELECT * FROM word WHERE wordType = (:wordType)")
    suspend fun findByWordType(wordType: Int): List<Word>
}