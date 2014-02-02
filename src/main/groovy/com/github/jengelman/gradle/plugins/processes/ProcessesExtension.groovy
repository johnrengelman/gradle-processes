package com.github.jengelman.gradle.plugins.processes

class ProcessesExtension implements ProcessOperations {

    @Delegate ProcessOperations processOperations

    ProcessesExtension(ProcessOperations processApi) {
        this.processOperations = processApi
    }
}
