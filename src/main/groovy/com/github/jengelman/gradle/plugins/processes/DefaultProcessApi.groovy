package com.github.jengelman.gradle.plugins.processes

import org.gradle.process.ExecResult

class DefaultProcessApi implements ProcessApi {

    @Override
    ProcessHandle javafork(Closure cl) {
        return null
    }

    @Override
    ProcessHandle fork(Closure cl) {
        return null
    }

    @Override
    ExecResult waitForFinish(ProcessHandle fork) {
        return null
    }

    @Override
    List<ExecResult> waitForFinish(List<ProcessHandle> forks) {
        return null
    }
}
