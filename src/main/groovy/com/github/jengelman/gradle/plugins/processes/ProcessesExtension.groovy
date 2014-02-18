package com.github.jengelman.gradle.plugins.processes

/**
 * Defines the Processes api that gets added to the Gradel project
 */
class ProcessesExtension implements ProcessOperations {

    @Delegate ProcessOperations processOperations

    ProcessesExtension(ProcessOperations processApi) {
        this.processOperations = processApi
    }
}
