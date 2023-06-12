package com.github.igr.sexyeditor.config

enum class BackgroundPosition(val value: Int, val label: String) {
    POSITION_TOP_LEFT(0, "Top-Left"),
    POSITION_TOP_MIDDLE(1, "Top-Middle"),
    POSITION_TOP_RIGHT(2, "Top-Right"),
    POSITION_MIDDLE_LEFT(3, "Middle-Left"),
    POSITION_CENTER(4, "Center"),
    POSITION_MIDDLE_RIGHT(5, "Middle-Right"),
    POSITION_BOTTOM_LEFT(6, "Bottom-Left"),
    POSITION_BOTTOM_MIDDLE(7, "Bottom-Middle"),
    POSITION_BOTTOM_RIGHT(8, "Bottom-Right");

    companion object {
        fun of(value: Int): BackgroundPosition = when (value) {
            0 -> POSITION_TOP_LEFT
            1 -> POSITION_TOP_MIDDLE
            2 -> POSITION_TOP_RIGHT
            3 -> POSITION_MIDDLE_LEFT
            4 -> POSITION_CENTER
            5 -> POSITION_MIDDLE_RIGHT
            6 -> POSITION_BOTTOM_LEFT
            7 -> POSITION_BOTTOM_MIDDLE
            8 -> POSITION_BOTTOM_RIGHT
            else -> throw IllegalArgumentException("Unknown value: $value")
        }
    }

    fun isOnBottom(): Boolean {
        return this == POSITION_BOTTOM_LEFT ||
                this == POSITION_BOTTOM_MIDDLE ||
                this == POSITION_BOTTOM_RIGHT
    }

    fun isHorizontallyCentered(): Boolean {
        return this == POSITION_MIDDLE_LEFT ||
                this == POSITION_CENTER ||
                this == POSITION_MIDDLE_RIGHT
    }

    fun isOnTop(): Boolean {
        return this == POSITION_TOP_LEFT ||
                this == POSITION_TOP_MIDDLE ||
                this == POSITION_TOP_RIGHT
    }

    fun isOnRightSide(): Boolean {
        return this == POSITION_TOP_RIGHT ||
                this == POSITION_MIDDLE_RIGHT ||
                this == POSITION_BOTTOM_RIGHT
    }

    fun isVerticallyCentered(): Boolean {
        return this == POSITION_TOP_MIDDLE ||
                this == POSITION_CENTER ||
                this == POSITION_BOTTOM_MIDDLE
    }

    fun isOnLeftSide(): Boolean {
        return this == POSITION_TOP_LEFT ||
                this == POSITION_MIDDLE_LEFT ||
                this == POSITION_BOTTOM_LEFT
    }
}
