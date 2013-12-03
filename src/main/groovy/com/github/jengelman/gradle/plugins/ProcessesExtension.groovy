package com.github.jengelman.gradle.plugins

import com.github.jengelman.gradle.plugins.processes.NonBlockingProcessApi
import com.github.jengelman.gradle.plugins.processes.ProcessHandle
import com.github.jengelman.gradle.plugins.processes.ProcessApi
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
