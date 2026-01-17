package com.sabo.core.toolkit

import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.TextFieldBuffer

object EmojiFilter : InputTransformation {
    override fun TextFieldBuffer.transformInput() {
        val filtered = asCharSequence().filter { char ->
            !Character.isHighSurrogate(char) &&
            !Character.isLowSurrogate(char) &&
            Character.getType(char) != Character.SURROGATE.toInt() &&
            Character.getType(char) != Character.OTHER_SYMBOL.toInt()
        }
        if (filtered.length != length) {
            replace(0, length, filtered.toString())
        }
    }
}
