package com.github.igr.sexyeditor.services

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.Service

@Service
class SexyEditorService {

    companion object {
        fun get(): SexyEditorService {
            return ApplicationManager.getApplication().getService(SexyEditorService::class.java)
        }
    }

}
