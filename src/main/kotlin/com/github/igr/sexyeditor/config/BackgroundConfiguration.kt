package com.github.igr.sexyeditor.config

enum class BackgroundPosition(val value: Int) {
    POSITION_TOP_LEFT(0),
    POSITION_TOP_MIDDLE(1),
    POSITION_TOP_RIGHT(2),
    POSITION_MIDDLE_LEFT(3),
    POSITION_CENTER(4),
    POSITION_MIDDLE_RIGHT(5),
    POSITION_BOTTOM_LEFT(6),
    POSITION_BOTTOM_MIDDLE(7),
    POSITION_BOTTOM_RIGHT(8);

    companion object {
        fun of(value: Int): BackgroundPosition {
            return when (value) {
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
    }
}

class BackgroundConfiguration {

    var name = "All editors"

    /**
     * List of matching editor file names for this background configuration.
     */
    var editorGroup = "*"

    /**
     * Opacity value.
     */
    var opacity = 0.10f

    /**
     * Image position.
     */
    var position = BackgroundPosition.POSITION_TOP_RIGHT

    /**
     * Position offset from the edges in pixels.
     */
    var positionOffset = 10

    /**
     * Is image shrunk.
     */
    var shrink = false

    /**
     * Amount of image shrinking in percents. 100% percent means shrink to fit
     * the screen dimensions.
     */
    var shrinkValue = 90

    /**
     * List of images.
     */
    var fileNames = emptyArray<String>()

    /**
     * Is the next image to load random one from the list.
     */
    var random = false

    /**
     * Slide-show mode.
     */
    var slideshow = false

    /**
     * Pause in milliseconds between two slides.
     */
    var slideshowPause = 3000

    /**
     * If background is located at fixed position and does not move with scrolling.
     */
    var fixedPosition = true

    var shrinkToFit = false

    override fun toString() = name
}