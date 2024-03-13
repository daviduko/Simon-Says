package com.example.simonsays

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View

class CustomView : View {
    private val redPaint: Paint = Paint().apply {
        color = Color.RED
        style = Paint.Style.FILL
    }
    private val greenPaint: Paint = Paint().apply {
        color = Color.GREEN
        style = Paint.Style.FILL
    }
    private val bluePaint: Paint = Paint().apply {
        color = Color.BLUE
        style = Paint.Style.FILL
    }
    private val yellowPaint: Paint = Paint().apply {
        color = Color.YELLOW
        style = Paint.Style.FILL
    }

    private var score: Int = 0
    private val textPaint: Paint = Paint().apply {
        color = Color.WHITE
        textSize = 100f
        textAlign = Paint.Align.CENTER
        typeface = Typeface.DEFAULT_BOLD
    }
    private val backgroundPaint: Paint = Paint().apply {
        color = Color.BLACK // Fondo negro
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
        // Aquí puedes realizar cualquier inicialización que necesites con los atributos.
    }

    fun updateScore(newScore: Int) {
        score = newScore
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val width = width
        val height = height
        val radius = width.coerceAtMost(height) / 4.toFloat() // Radio de los círculos
        val centerX = width / 2.toFloat()
        val centerY = height / 2.toFloat()
        // Dibuja los círculos de colores centrados
        canvas.drawCircle(centerX - radius, centerY - radius, radius, redPaint) // Rojo
        canvas.drawCircle(centerX + radius, centerY - radius, radius, greenPaint) // Verde
        canvas.drawCircle(centerX - radius, centerY + radius, radius, bluePaint) // Azul
        canvas.drawCircle(centerX + radius, centerY + radius, radius, yellowPaint) // Amarillo

        val backgroundRect = RectF(0f, 0f, width.toFloat(), height.toFloat() / 5f)
        canvas.drawRect(backgroundRect, backgroundPaint)
        canvas.drawRect(backgroundRect, borderPaint)

        val textX = width / 2.toFloat()
        val textY = height / 8.toFloat() + textPaint.textSize / 3 // Ajusta la posición vertical
        canvas.drawText(score.toString(), textX, textY, textPaint)
    }
}