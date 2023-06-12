package com.github.igr.sexyeditor.ui

import javax.swing.ImageIcon

class PluginIcons {
    companion object {
        val IMAGES = iconOf("images")
        val POSITIONS = arrayOf(
            iconOf("top-left"),
            iconOf("top-middle"),
            iconOf("top-right"),
            iconOf("middle-left"),
            iconOf("center"),
            iconOf("middle-right"),
            iconOf("bottom-left"),
            iconOf("bottom-middle"),
            iconOf("bottom-right"),
        )
        val LOGO = iconOf("sexyeditor")
        val TIP = iconOf("hint")
    }
}

private fun iconOf(name: String): ImageIcon {
    val path = "/icons/$name.png"
    val url = PluginIcons::class.java.getResource(path)
    return ImageIcon(url)
}