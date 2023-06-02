package com.github.igr.sexyeditor.services

import com.github.igr.sexyeditor.SexyEditorBundle
import com.intellij.openapi.components.Service
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.project.Project

@Service(Service.Level.PROJECT)
class SexyEditorProjectService(project: Project) {

    init {
        thisLogger().info(SexyEditorBundle.message("projectService", project.name))
        thisLogger().warn("Don't forget to remove all non-needed sample code files with their corresponding registration entries in `plugin.xml`.")
    }

    fun getRandomNumber() = (1..100).random()
}
