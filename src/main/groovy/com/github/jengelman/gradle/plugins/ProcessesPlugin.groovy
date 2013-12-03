package com.github.jengelman.gradle.plugins

import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * A Gradle plugin that provides capabilities for creating and managing external processes.
 *
 * Allows processes to be executed in a blocking, synchronous fashion by waiting for their termination and exit value or
 * non-blocking asynchronous fashion by forking the process and later waiting for its termination.
 */
class ProcessesPlugin implements Plugin<Project> {

    static final String PROCESSES_EXTENSION = 'procs'

    @Override
    void apply(Project project) {
        project.extensions.create(PROCESSES_EXTENSION, ProcessesExtension)
    }
}