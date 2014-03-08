package com.github.jengelman.gradle.plugins.processes;

import org.gradle.process.ExecResult;

/**
 * Callbacks for process state
 */
public interface ProcessHandleListener {

    void executionStarted(ProcessHandle processHandle);

    void executionFinished(ProcessHandle processHandle, ExecResult execResult);
}
