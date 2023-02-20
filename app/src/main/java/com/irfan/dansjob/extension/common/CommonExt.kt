package com.irfan.dansjob.extension.common

inline fun <reified T : Any> clazz() = T::class.java

inline fun <reified T : Any> tag() = T::class.java.simpleName