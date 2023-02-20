package com.irfan.dansjob.extension.fragment

import androidx.fragment.app.Fragment
import com.irfan.dansjob.extension.activty.toast

fun Fragment.toast(message: String) {
    activity?.toast(message)
}