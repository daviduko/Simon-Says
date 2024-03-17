package com.example.simonsays

import android.graphics.Color
import android.graphics.Paint

class Button(buttonColor: Int, buttonStyle: Paint.Style) {

    val paint: Paint = Paint().apply {
        color = buttonColor
        style = buttonStyle
    }


}