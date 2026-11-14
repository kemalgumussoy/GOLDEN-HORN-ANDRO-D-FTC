package com.kemal.goldenhorn

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

// Dil yöneticisi — TR/EN toggle. Compose otomatik recompose eder.
object LangManager {
    var isTurkish by mutableStateOf(true)

    fun t(tr: String, en: String): String = if (isTurkish) tr else en

    fun toggle() { isTurkish = !isTurkish }
}
