package com.irfan.dansjob.extension.primitive

fun Boolean.switchOnOff(): String = when (this) {
    true -> "ON"
    else -> "OFF"
}