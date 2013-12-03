package com.github.jengelman.gradle.plugins.processes

import com.github.jengelman.gradle.plugins.processes.internal.NonBlockingProcessApi
import org.gradle.process.ExecResult

class ProcessesExtension implements NonBlockingProcessApi {

    ProcessApi processApi

    ProcessesExtension(ProcessApi processApi) {
        this.processApi = processApi
    }

    @Override
    ProcessHandle javafork(Closure cl) {
        return processApi.javafork(cl)
    }

    @Override
    ProcessHandle fork(Closure cl) {
        return processApi.javafork(cl)
    }

    @Override
    ExecResult waitForFinish(ProcessHandle fork) {
        return processApi.waitForFinish(fork)
    }

    @Override
    List<ExecResult> waitForFinish(List<ProcessHandle> forks) {
        return processApi.waitForFinish(forks)
    }
}
