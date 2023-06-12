package com.github.igr.sexyeditor.config

enum class FitType(val value: Int, val label: String) {
    NONE(0, "None"),
    SHRINK_TO_FIT(1, "Shrink to fit"),
    RESIZE_TO_FIT(2, "Resize to fit"),
    FILL(3, "Fill");

    override fun toString(): String {
        return label
    }

    companion object {
        fun of(value: Int): FitType = when (value) {
            0 -> NONE
            1 -> SHRINK_TO_FIT
            2 -> RESIZE_TO_FIT
            3 -> FILL
            else -> throw IllegalArgumentException("Unknown value: $value")
        }
    }

}