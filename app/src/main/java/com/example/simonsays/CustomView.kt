package com.example.simonsays

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface
import android.media.AudioAttributes
import android.media.SoundPool
import android.os.Handler
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import java.util.Dictionary
import java.util.Random

class CustomView : View {
    private lateinit var soundPool: SoundPool
    private val random = Random()
    private val handler = Handler()

    private var soundIds = mutableListOf<Int>()
    private val sequence: MutableList<Int> = mutableListOf()
    private var sequenceIndex: Int = 0
    private var playerSequenceIndex: Int = 0
    private var level: Int = 1
    private var score: Int = 0
    private var isPlayingSequence: Boolean = false
    private var isPlaying: Boolean = false
    private val colors = arrayOf(Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW)

    private val sequencePaint: Paint = Paint().apply {
        color = Color.WHITE
        style = Paint.Style.FILL
    }

    private val textPaint: Paint = Paint().apply {
        color = Color.WHITE
        textSize = 100f
        textAlign = Paint.Align.CENTER
        typeface = Typeface.DEFAULT_BOLD
    }
    private val backgroundPaint: Paint = Paint().apply {
        color = Color.BLACK
    }
    private val borderPaint: Paint = Paint().apply {
        color = Color.WHITE
        style = Paint.Style.STROKE
        strokeWidth = 6f
    }

    constructor(context: Context) : super(context) {
        init(null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        initSoundPool()
        resetGame()
    }

    private fun initSoundPool() {
        soundPool = SoundPool.Builder()
            .setMaxStreams(4)
            .setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build()
            )
            .build()

        soundIds.add(soundPool.load(context, R.raw.red_sound, 1))
        soundIds.add(soundPool.load(context, R.raw.green_sound, 1))
        soundIds.add(soundPool.load(context, R.raw.blue_sound, 1))
        soundIds.add(soundPool.load(context, R.raw.yellow_sound, 1))
    }

    private fun resetGame() {
        sequence.clear()
        score = 0
        level = 1
    }

    private fun addToSequence() {
        for (i in 0 until level) {
            sequence.add(random.nextInt(4))
        }
    }

    fun startGame() {
        isPlaying = true
        resetGame()
        playSequence()
    }

    private fun playSequence() {
        invalidate()
        addToSequence()
        isPlayingSequence = true
        sequenceIndex = 0
        playerSequenceIndex = 0
        showNextColor()
    }
    private fun showNextColor() {
        if (sequenceIndex < sequence.size) {
            val currentColor = sequence[sequenceIndex]
            soundPool.play(soundIds[currentColor], 1.0f, 1.0f, 1, 0, 1.0f)
            sequencePaint.color = colors[currentColor]
            invalidate()
            handler.postDelayed({
                sequencePaint.color = Color.WHITE
                invalidate()
                sequenceIndex++
                showNextColor()
            }, 500)
        } else {
            isPlayingSequence = false
            invalidate()
        }
    }


    fun updateScore() {
        score++
        invalidate()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (!isPlayingSequence && isPlaying && (playerSequenceIndex < sequence.size)) {
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    val x = event.x
                    val y = event.y
                    val quadrant = getQuadrantTouched(x, y)
                    checkButton(quadrant)
                }
            }
        }
        return super.onTouchEvent(event)
    }

    private fun getQuadrantTouched(x: Float, y: Float): Int {
        val centerX = width / 2
        val centerY = height / 2
        val quadrantX = x > centerX
        val quadrantY = y > centerY

        return if (quadrantX && !quadrantY) {
            0 // Top right (red)
        } else if (!quadrantX && !quadrantY) {
            1 // Top left (green)
        } else if (quadrantX && quadrantY) {
            3 // Bottom right (yellow)
        } else {
            2 // Bottom left (blue)
        }
    }

    private fun checkButton(quadrant: Int) {
        if(sequence[playerSequenceIndex] != quadrant){
            startGame()
        } else{
            playerSequenceIndex++
            if(playerSequenceIndex == sequence.size){
                updateScore()
                level++
                playSequence()
            }
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val width = width
        val height = height
        val radius = width.coerceAtMost(height) / 4.toFloat()
        val centerX = width / 2.toFloat()
        val centerY = height / 2.toFloat()

        if (isPlayingSequence) {
            when(sequencePaint.color){
                Color.RED -> canvas.drawCircle(centerX - radius, centerY - radius, radius, sequencePaint)
                Color.GREEN -> canvas.drawCircle(centerX + radius, centerY - radius, radius, sequencePaint)
                Color.BLUE -> canvas.drawCircle(centerX - radius, centerY + radius, radius, sequencePaint)
                Color.YELLOW -> canvas.drawCircle(centerX + radius, centerY + radius, radius, sequencePaint)
            }
        } else {
            for (i in 0 until 4) {
                val color = colors[i]
                val paint = Paint().apply {
                    this.color = color
                    style = Paint.Style.FILL
                }
                canvas.drawCircle(
                    centerX + (i % 2 * 2 - 1) * radius,
                    centerY + (i / 2 * 2 - 1) * radius,
                    radius,
                    paint
                )
            }
        }

        val backgroundRect = RectF(0f, 0f, width.toFloat(), height.toFloat() / 5f)
        canvas.drawRect(backgroundRect, backgroundPaint)
        canvas.drawRect(backgroundRect, borderPaint)

        val textX = width / 2.toFloat()
        val textY = height / 8.toFloat() + textPaint.textSize / 3 // Ajusta la posición vertical
        canvas.drawText(score.toString(), textX, textY, textPaint)
    }
}