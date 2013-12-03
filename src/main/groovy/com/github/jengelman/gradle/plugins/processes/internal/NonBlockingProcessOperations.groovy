package com.github.jengelman.gradle.plugins.processes.internal

import com.github.jengelman.gradle.plugins.processes.ProcessHandle
import org.gradle.process.ExecResult

/**
 * Methods for interacting with processes in a non-blocking way.
 */
public interface NonBlockingProcessOperations {

    ProcessHandle javafork(Closure cl)

    ProcessHandle fork(Closure cl)

    ExecResult waitForFinish(ProcessHandle fork)

    List<ExecResult> waitForFinish(List<ProcessHandle> forks)

}