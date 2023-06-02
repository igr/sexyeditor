package com.github.igr.sexyeditor.services

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.Service

@Service(Service.Level.PROJECT)
class BackgroundService {

    companion object {
        fun get(): BackgroundService {
            return ApplicationManager.getApplication().getService(BackgroundService::class.java)
        }
    }
}