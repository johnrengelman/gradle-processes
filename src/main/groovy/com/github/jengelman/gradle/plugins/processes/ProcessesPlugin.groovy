package com.github.jengelman.gradle.plugins.processes

import com.github.jengelman.gradle.plugins.processes.internal.DefaultProcessApi
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.internal.file.FileResolver
import org.gradle.internal.reflect.Instantiator

import javax.inject.Inject

/**
 * A Gradle plugin that provides capabilities for creating and managing external processes.
 *
 * Allows processes to be executed in a blocking, synchronous fashion by waiting for their termination and exit value or
 * non-blocking asynchronous fashion by forking the process and later waiting for its termination.
 */
class ProcessesPlugin implements Plugin<Project> {

    static final String PROCESSES_EXTENSION = 'procs'

    ProcessApi processApi

    @Inject
    ProcessesPlugin(Instantiator instantiator, FileResolver fileResolver) {
        processApi = new DefaultProcessApi(instantiator, fileResolver)
    }

    @Override
    void apply(Project project) {
        project.extensions.create(PROCESSES_EXTENSION, ProcessesExtension, processApi)
    }
}
