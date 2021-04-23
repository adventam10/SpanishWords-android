package com.example.spanishwords

enum class Category constructor(val wordType: Int) {
    NOUN(0),
    ADJECTIVE(1),
    VERB(2),
    PREPOSITION(3),
    ARTICLE(4),
    PRONOUN(5),
    NUMBER(6),
    MONTHANDWEEK(7),
    INTERROGATIVE(8),
    ADVERB(9);

    companion object {
        fun fromWordType(wordType: Int): Category {
            return values().firstOrNull { it.wordType == wordType } ?: NOUN
        }
    }

    val nameId: Int
        get() = when(this) {
            NOUN -> R.string.category_noun
            ADJECTIVE -> R.string.category_adjective
            VERB -> R.string.category_verb
            PREPOSITION -> R.string.category_preposition
            ARTICLE -> R.string.category_article
            PRONOUN -> R.string.category_pronoun
            NUMBER -> R.string.category_number
            MONTHANDWEEK -> R.string.category_month_and_week
            INTERROGATIVE -> R.string.category_interrogative
            ADVERB -> R.string.category_adverb
        }
}
