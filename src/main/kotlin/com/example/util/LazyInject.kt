package com.example.util

import org.koin.java.KoinJavaComponent
import org.koin.java.KoinJavaComponent.inject

inline fun <reified T : Any> lazyInject() =
    inject<T>(T::class.java)