package com.github.igr.sexyeditor.ui.editor

import com.intellij.uiDesigner.core.GridConstraints
import com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER
import com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL
import com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW
import com.intellij.uiDesigner.core.Spacer
import javax.swing.JPanel

fun uiCreateSpacer3(panel: JPanel) {
    panel.add(
        Spacer(),
        GridConstraints(
            10, 3, 1, 1,
            ANCHOR_CENTER, FILL_HORIZONTAL, SIZEPOLICY_WANT_GROW, 1,
            null, null, null, 0, false
        )
    )
}