package com.example.spanishwords

import android.os.Bundle
import android.os.Handler
import android.view.MenuItem
import android.view.MotionEvent
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class FlashCardActivity : AppCompatActivity() {

    private lateinit var seekBar: SeekBar
    private lateinit var playButton: ImageButton
    private lateinit var nextButton: ImageButton
    private lateinit var previousButton: ImageButton
    private lateinit var completedCheckBox: CheckBox
    private lateinit var pronunciationCheckBox: CheckBox
    private lateinit var pronunciationText: TextView
    private lateinit var wordText: TextView
    private lateinit var speedText: TextView
    private lateinit var pauseLayout: ConstraintLayout
    private lateinit var noDataText: TextView

    private var speed: Long = 0
    private val handler = Handler()
    private lateinit var runnable: Runnable

    private var currentIndex = -1
    private val wordIndex
      get() = currentIndex/2
    private val existsNextWord: Boolean
      get() = words.lastIndex*2 >= currentIndex
    private val existsPreviousWord: Boolean
        get() = 0 < currentIndex
    private var words: MutableList<Word> = mutableListOf()

    private lateinit var wordRepository: WordRepository
    private lateinit var flashCardConfigRepository: FlashCardConfigRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flash_card)

        val index = intent.getIntExtra("index", 0)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        if (index == 0) {
            supportActionBar?.title = "すべて"
        } else {
            supportActionBar?.title = getString(Category.fromWordType(index-1).nameId)
        }
        wordRepository = WordRepository(applicationContext)
        flashCardConfigRepository = FlashCardConfigRepository(applicationContext)

        findViews()
        setupButtons()
        speed = (flashCardConfigRepository.speed * 1000).toLong()

        noDataText.isVisible = false

        val isShown = flashCardConfigRepository.isPronunciationShown
        pronunciationText.isVisible = isShown
        pronunciationCheckBox.isChecked = isShown

        seekBar.progress = flashCardConfigRepository.speedIndex
        speedText.text = flashCardConfigRepository.speed.toString()
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                flashCardConfigRepository.speedIndex = progress
                speedText.text = flashCardConfigRepository.speed.toString()
                speed = (flashCardConfigRepository.speed * 1000).toLong()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
            }
        })

        pauseLayout.isVisible = false

        runnable = object : Runnable {
            override fun run() {
                if (existsNextWord) {
                    currentIndex++
                    displayWord()
                    handler.postDelayed(this, speed)
                } else {
                    stopTimer()
                }
            }
        }

        GlobalScope.launch(Dispatchers.Main) {
            val targetWords = if (index == 0) {
                wordRepository.fetchAll()
            } else {
                wordRepository.findByWordType(index-1)
            }
            words.clear()
            words.addAll(targetWords.filter { !it.isChecked })
            if (words.isEmpty()) {
                noDataText.isVisible = true
                pauseLayout.isVisible = false
            } else {
                startTimer()
            }
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

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_DOWN) {
            stopTimer()
        }
        return super.onTouchEvent(event)
    }

    private fun findViews() {
        seekBar = findViewById(R.id.seek_bar)
        playButton = findViewById(R.id.image_button_play)
        nextButton = findViewById(R.id.image_button_next)
        previousButton = findViewById(R.id.image_button_previous)
        completedCheckBox = findViewById(R.id.check_box_completed)
        pronunciationCheckBox = findViewById(R.id.check_box_pronunciation)
        pronunciationText = findViewById(R.id.text_pronunciation)
        wordText = findViewById(R.id.text_word)
        speedText = findViewById(R.id.text_speed)
        pauseLayout = findViewById(R.id.layout_pause)
        noDataText = findViewById(R.id.text_no_data)
    }

    private fun setupButtons() {
        playButton.setOnClickListener {
            startTimer()
        }

        nextButton.setOnClickListener {
            if (existsNextWord) {
                currentIndex++
                displayWord()
            }
        }

        previousButton.setOnClickListener {
            if (existsPreviousWord) {
                currentIndex--
                displayWord()
            }
        }

        completedCheckBox.setOnCheckedChangeListener { _, isChecked ->
            GlobalScope.launch(Dispatchers.Main) {
                val word = words[wordIndex]
                val newWord = wordRepository.updateIsChecked(isChecked, word)
                words[wordIndex] = newWord
            }
        }

        pronunciationCheckBox.setOnCheckedChangeListener { _, isChecked ->
            pronunciationText.isVisible = isChecked
            flashCardConfigRepository.isPronunciationShown = isChecked
        }
    }

    private fun startTimer() {
        pauseLayout.isVisible = false
        handler.post(runnable)
    }

    private fun stopTimer() {
        pauseLayout.isVisible = true
        handler.removeCallbacks(runnable)
    }

    private fun displayWord() {
        val word = words[wordIndex]
        completedCheckBox.isChecked = word.isChecked
        if (currentIndex%2 == 0) {
            wordText.text = word.spanish
            pronunciationText.text = word.pronunciation
        } else {
            wordText.text = word.japanese
            pronunciationText.text = ""
        }
    }
}