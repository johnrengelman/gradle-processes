package com.github.jengelman.gradle.plugins.processes.internal;

import com.github.jengelman.gradle.plugins.processes.ProcessHandle;
import org.gradle.process.ExecResult;
import org.gradle.process.internal.ExecHandle;
import org.gradle.process.internal.ExecHandleState;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * Wrapper class for converting internal ExecHandler reference to a ProcessHandle
 */
public class ExecHandleWrapper implements ProcessHandle {

    private final ExecHandle execHandle;
    private final boolean ignoreExitValue;

    public ExecHandleWrapper(ExecHandle execHandle) {
        this(execHandle, false);
    }

    public ExecHandleWrapper(ExecHandle execHandle, boolean ignoreExitValue) {
        this.execHandle = execHandle;
        this.ignoreExitValue = ignoreExitValue;
    }

    @Override
    public File getDirectory() {
        return execHandle.getDirectory();
    }

    @Override
    public String getCommand() {
        return execHandle.getCommand();
    }

    @Override
    public List<String> getArguments() {
        return execHandle.getArguments();
    }

    @Override
    public Map<String, String> getEnvironment() {
        return execHandle.getEnvironment();
    }

    @Override
    public ExecHandleState getState() {
        return execHandle.getState();
    }

    @Override
    public ExecResult waitForFinish() {
        return execHandle.waitForFinish();
    }

    @Override
    public boolean isIgnoreExitValue() {
        return ignoreExitValue;
    }

    public static ProcessHandle wrap(ExecHandle execHandle) {
        return new ExecHandleWrapper(execHandle);
    }

    public static ProcessHandle wrap(ExecHandle execHandle, boolean ignoreExitValue) {
        return new ExecHandleWrapper(execHandle, ignoreExitValue);
    }
}
