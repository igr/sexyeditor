package com.github.igr.sexyeditor.ui

import javax.swing.ImageIcon

class PluginIcons {
    companion object {
        val MOVE_DOWN = actionIconOf("moveDown")
        val MOVE_UP = actionIconOf("moveUp")
        val REMOVE = generalIconOf("remove")
        val ADD = generalIconOf("add")
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
    }
}

private fun iconOf(name: String): ImageIcon {
    val path = "/icons/$name.png"
    val url = PluginIcons::class.java.getResource(path)
    return ImageIcon(url)
}

private fun generalIconOf(name: String): ImageIcon {
    val path = "/general/$name.png"
    val url = PluginIcons::class.java.getResource(path)
    return ImageIcon(url)
}

private fun actionIconOf(name: String): ImageIcon {
    val path = "/actions/$name.png"
    val url = PluginIcons::class.java.getResource(path)
    return ImageIcon(url)
}
