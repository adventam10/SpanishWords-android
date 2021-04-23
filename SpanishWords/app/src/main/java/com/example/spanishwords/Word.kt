package com.example.spanishwords

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Word(
    @PrimaryKey val id: String,
    val spanish: String,
    val pronunciation: String,
    val japanese: String,
    val wordType: Int,
    val isChecked: Boolean
    )