package com.example.spanishwords

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.ListView

class CategoryActivity : AppCompatActivity() {

    private lateinit var listView: ListView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.screen_title_category)

        listView = findViewById(R.id.list)
        listView.adapter = ArrayAdapter(this,
                android.R.layout.simple_list_item_1,
            Category.values().map { getString(it.nameId) })
        listView.setOnItemClickListener { _, _, position, _ ->
            val intent = Intent(this, WordListActivity::class.java).apply {
                putExtra("index", position)
            }
            startActivity(intent)
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