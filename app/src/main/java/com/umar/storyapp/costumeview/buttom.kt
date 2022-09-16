package com.umar.storyapp.costumeview

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.Gravity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import com.umar.storyapp.R

class buttom : AppCompatButton {

    private lateinit var enableBaground: Drawable
    private lateinit var disableBacground: Drawable

    private var txtColor: Int = 0

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    // Metode onDraw() digunakan untuk mengcustom button ketika enable dan disable
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        // Mengubah background dari Button
        background = if (isEnabled) enableBaground else disableBacground

        // Mengubah warna text pada button
        setTextColor(txtColor)

        // Mengubah ukuran text pada button
        textSize = 12f

        // Menjadikan object pada button menjadi center
        gravity = Gravity.CENTER

        // Mengubah text pada button pada kondisi enable dan disable
        text = if (isEnabled) "Submit" else "Isi Dulu"
    }

    // pemanggilan Resource harus dilakukan saat kelas MyButton diinisialisasi, jadi harus dikeluarkan dari metode onDraw
    private fun init() {
        txtColor = ContextCompat.getColor(context, android.R.color.background_light)
        enableBaground = ContextCompat.getDrawable(context, R.drawable.buttom) as Drawable
        disableBacground = ContextCompat.getDrawable(context, R.drawable.buttom_ds) as Drawable
    }
}