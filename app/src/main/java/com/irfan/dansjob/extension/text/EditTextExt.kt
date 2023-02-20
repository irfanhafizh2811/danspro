package com.irfan.dansjob.extension.text

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

fun EditText.charCountingListener(listener: (Int) -> Unit) {
    addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun afterTextChanged(p0: Editable?) {
            listener(text.length)
        }
    })
}

fun String.numberArabic(): String {
    val arabicChars: CharArray = charArrayOf('٠', '١', '٢', '٣', '٤', '٥', '٦', '٧', '٨', '٩')
    val builder = StringBuilder()
    val inc = 1
    for (index in 0..this.length step inc) {
        val isDigit = Character.isDigit(this.toCharArray()[index])
        if (isDigit) builder.append(arabicChars[(this.toCharArray()[index]).code - 48])
        else builder.append(this.toCharArray()[index])
    }
    return builder.toString()
}