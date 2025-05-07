package com.github.igr.sexyeditor.ui

import javax.swing.ImageIcon

data class ImageFile(
    val path: String
) {
    val icon: ImageIcon by lazy {
        return@lazy loadImageAsIcon() ?: PluginIcons.WARNING
    }
}
