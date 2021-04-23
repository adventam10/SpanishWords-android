package com.example.spanishwords

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var seekBar: SeekBar
    private lateinit var spinner: Spinner
    private lateinit var startButton: Button
    private lateinit var speedText: TextView
    private lateinit var flashCardConfigRepository: FlashCardConfigRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.title = getString(R.string.screen_title_main)
        MobileAds.initialize(this) {}
        val adView = findViewById<AdView>(R.id.adView)
//        adView.adUnitId = "ca-app-pub-8666562449597082/8457304205"
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)

        setupDefaultWords()
        flashCardConfigRepository = FlashCardConfigRepository(applicationContext)

        findViews()
        setupSeekBar()
        setupSpeed()
        setupSpinner()
        startButton.setOnClickListener {
            val intent = Intent(this, FlashCardActivity::class.java).apply {
                putExtra("index", spinner.selectedItemPosition)
            }
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()
        setupSpeed()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.category -> {
                val intent = Intent(this, CategoryActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.license -> {
                startActivity(Intent(this, OssLicensesMenuActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun findViews() {
        seekBar = findViewById(R.id.seek_bar)
        spinner = findViewById(R.id.spinner)
        startButton = findViewById(R.id.button_start)
        speedText = findViewById(R.id.text_speed)
    }

    private fun setupSpeed() {
        seekBar.progress = flashCardConfigRepository.speedIndex
        speedText.text = flashCardConfigRepository.speed.toString()
    }

    private fun setupSeekBar() {
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                flashCardConfigRepository.speedIndex = progress
                speedText.text = flashCardConfigRepository.speed.toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
            }
        })
    }

    private fun setupSpinner() {
        val spinnerItems = listOf("すべて") + Category.values().map { getString(it.nameId) }
        val adapter = ArrayAdapter(applicationContext, android.R.layout.simple_spinner_item, spinnerItems)
        spinner.adapter = adapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }

    private fun setupDefaultWords() {
        val defaultWordRepository = DefaultWordRepository(applicationContext)
        val wordRepository = WordRepository(applicationContext)
        GlobalScope.launch(Dispatchers.Main) {
            if (wordRepository.exists()) {
                return@launch
            }
            val words = defaultWordRepository.loadDefaultWords()
            wordRepository.insertWords(words)
        }
    }
}