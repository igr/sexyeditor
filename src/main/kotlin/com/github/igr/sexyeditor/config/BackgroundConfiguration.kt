package com.github.igr.sexyeditor.config

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
     * Is image resized.
     */
    var resize = false

    /**
     * Amount of image shrinking in percents. 100% percent means shrink to fit
     * the screen dimensions.
     */
    var resizeValue = 90

    /**
     * List of background images.
     */
    var fileNames = emptyArray<String>()

    /**
     * Is the next image to load random.
     */
    var random = false

    /**
     * Slide-show mode.
     */
    var slideshow = false

    /**
     * Pause in seconds between two slides during the slideshow.
     */
    var slideshowPause = 3

    /**
     * If background is located at fixed position and does not move with scrolling.
     */
    var fixedPosition = true

    /**
     * How to fit the background image.
     */
    var fitType = FitType.NONE

    override fun toString() = name
}