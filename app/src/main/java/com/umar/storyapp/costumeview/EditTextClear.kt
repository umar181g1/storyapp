package com.umar.storyapp.costumeview

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.umar.storyapp.R

class EditTextClear : AppCompatEditText, View.OnTouchListener {
    private lateinit var clear: Drawable

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

    private fun init() {
        clear = ContextCompat.getDrawable(context, R.drawable.ic_baseline_clear_24) as Drawable

        setOnTouchListener(this)

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //do nothing
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (text.toString().isNotEmpty()) showClear() else hidenClear()
            }

            override fun afterTextChanged(p0: Editable?) {
                //do nothng
            }

        })
    }

    // Menampilkan clear button
    private fun showClear() {
        setButtonDrawables(endOfTheText = clear)
    }

    // Menghilangkan clear button
    private fun hidenClear() {
        setButtonDrawables()
    }

    //Mengkonfigurasi button
    private fun setButtonDrawables(
        startOfTheText: Drawable? = null,
        topOfTheText: Drawable? = null,
        endOfTheText: Drawable? = null,
        bottomOfTheText: Drawable? = null
    ) {
        // Sets the Drawables (if any) to appear to the left of,
        // above, to the right of, and below the text.
        setCompoundDrawablesWithIntrinsicBounds(
            startOfTheText,
            topOfTheText,
            endOfTheText,
            bottomOfTheText
        )
    }

    override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
        if (compoundDrawables[2] != null) {
            val clearButtomStart: Float
            val clearButtomEnd: Float
            var isClearButtonClicked = false

            if (layoutDirection == View.LAYOUT_DIRECTION_RTL) {
                clearButtomEnd = (clear.intrinsicWidth + paddingStart).toFloat()
                if (p1 != null) {
                    when {
                        p1.x < clearButtomEnd -> isClearButtonClicked = true
                    }
                }
            } else {
                clearButtomStart = (width - paddingLeft - clear.intrinsicWidth).toFloat()
                if (p1 != null) {
                    when {
                        p1.x > clearButtomStart -> isClearButtonClicked = true
                    }
                }
                if (isClearButtonClicked) {
                    if (p1 != null) {
                        when (p1.action) {
                            MotionEvent.ACTION_DOWN -> {
                                clear = ContextCompat.getDrawable(
                                    context,
                                    R.drawable.ic_baseline_clear_24
                                ) as Drawable
                                showClear()
                                return true
                            }
                            MotionEvent.ACTION_UP -> {
                                clear = ContextCompat.getDrawable(
                                    context,
                                    R.drawable.ic_baseline_clear_24
                                ) as Drawable
                                when {
                                    text != null -> text!!.clear()
                                }
                                hidenClear()
                                return true
                            }
                            else -> return false
                        }

                    }
                } else return false
            }
        }
        return false
    }
}