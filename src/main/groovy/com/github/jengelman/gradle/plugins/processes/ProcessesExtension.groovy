package com.github.jengelman.gradle.plugins.processes

import org.gradle.process.ExecResult

class ProcessesExtension implements ProcessOperations {

    ProcessOperations processOperations

    ProcessesExtension(ProcessOperations processApi) {
        this.processOperations = processApi
    }

    @Override
    ProcessHandle javafork(Closure cl) {
        return processOperations.javafork(cl)
    }

    @Override
    ProcessHandle fork(Closure cl) {
        return processOperations.fork(cl)
    }

    @Override
    ExecResult waitForFinish(ProcessHandle fork) {
        return processOperations.waitForFinish(fork)
    }

    @Override
    List<ExecResult> waitForFinish(List<ProcessHandle> forks) {
        return processOperations.waitForFinish(forks)
    }
}
