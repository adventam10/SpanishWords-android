package com.example.spanishwords

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.ListView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class WordListActivity : AppCompatActivity() {

    private lateinit var listView: ListView
    private lateinit var wordRepository: WordRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_word_list)

        val index = intent.getIntExtra("index", 0)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(Category.fromWordType(index).nameId)

        wordRepository = WordRepository(applicationContext)
        listView = findViewById(R.id.list)
        val adapter = WordAdapter(
            this,
            mutableListOf()
        )
        listView.adapter = adapter
        listView.setOnItemClickListener { _, _, position, _ ->
            GlobalScope.launch(Dispatchers.Main) {
                val word = adapter.items[position]
                val newWord = wordRepository.updateIsChecked(!word.isChecked, word)
                adapter.items[position] = newWord
                adapter.notifyDataSetChanged()
            }
        }
        GlobalScope.launch(Dispatchers.Main) {
            adapter.items.clear()
            adapter.items.addAll(wordRepository.findByWordType(index))
            adapter.notifyDataSetChanged()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home->{
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}