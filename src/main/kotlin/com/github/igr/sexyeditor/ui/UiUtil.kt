package com.github.igr.sexyeditor.ui

import com.github.igr.sexyeditor.SexyEditor
import javax.swing.ImageIcon

fun iconOf(name: String): ImageIcon {
    val path = "/icons/$name.png"
    val url = SexyEditor::class.java.getResource(path)
    return ImageIcon(url)
}

fun generalIconOf(name: String): ImageIcon {
    val path = "/general/$name.png"
    val url = SexyEditor::class.java.getResource(path)
    return ImageIcon(url)
}

fun actionIconOf(name: String): ImageIcon {
    val path = "/actions/$name.png"
    val url = SexyEditor::class.java.getResource(path)
    return ImageIcon(url)
}
