package com.github.igr.sexyeditor.services

import com.github.igr.sexyeditor.config.BackgroundConfiguration
import com.github.igr.sexyeditor.listeners.BackgroundBorder
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.Service
import com.intellij.openapi.fileTypes.WildcardFileNameMatcher
import com.intellij.util.containers.WeakList
import java.util.*

@Service
class SexyEditorService {

    companion object {
        fun get(): SexyEditorService {
            return ApplicationManager.getApplication().getService(SexyEditorService::class.java)
        }
    }

    fun matchFileName(config: BackgroundConfiguration, fileName: String): Boolean {
        val st = StringTokenizer(config.editorGroup, ";")
        while (st.hasMoreTokens()) {
            val token = st.nextToken().trim { it <= ' ' }
            if (WildcardFileNameMatcher(token).acceptsCharSequence(fileName)) {
                return true
            }
        }
        return false
    }

    // ---------------------------------------------------------------- borders and thread

    private val allBorders: WeakList<BackgroundBorder> = WeakList()

    fun registerBorder(backgroundBorder: BackgroundBorder) {
        allBorders.add(backgroundBorder);
    }

    fun unregisterBorder(backgroundBorder: BackgroundBorder) {
        allBorders.remove(backgroundBorder);
    }


}
