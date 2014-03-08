package com.github.jengelman.gradle.plugins.processes.internal;

import com.github.jengelman.gradle.plugins.processes.ProcessHandleListener;
import org.gradle.process.ExecResult;
import org.gradle.process.internal.ExecHandle;
import org.gradle.process.internal.ExecHandleListener;

/**
 * Adapter for hiding internal Gradle class from API
 */
public class ExecHandleListenerAdapter implements ExecHandleListener {

    ProcessHandleListener processHandleListener;

    ExecHandleListenerAdapter(ProcessHandleListener processHandleListener) {
        this.processHandleListener = processHandleListener;
    }
    @Override
    public void executionStarted(ExecHandle execHandle) {
        processHandleListener.executionStarted(new ExecHandleWrapper(execHandle));
    }

    @Override
    public void executionFinished(ExecHandle execHandle, ExecResult execResult) {
        processHandleListener.executionFinished(new ExecHandleWrapper(execHandle), execResult);
    }
}
