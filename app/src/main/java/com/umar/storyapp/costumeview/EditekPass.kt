package com.umar.storyapp.costumeview

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import com.umar.storyapp.R

class EditekPass : AppCompatEditText {

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
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                // Do nothing
            }

            override fun onTextChanged(text: CharSequence, p1: Int, p2: Int, p3: Int) {
                error =
                    if (text.isNotEmpty() && text.toString().length < 6) context.getString(R.string.passwor_hrus) else null
            }

            override fun afterTextChanged(p0: Editable?) {
                // Do nothing
            }

        })
    }
}