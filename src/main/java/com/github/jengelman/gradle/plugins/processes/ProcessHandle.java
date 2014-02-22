package com.github.jengelman.gradle.plugins.processes;

import org.gradle.process.ExecResult;
import org.gradle.process.internal.ExecHandleState;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * Represents an executing process.
 */
public interface ProcessHandle {

    /**
     * The working directory of the process.
     * @return File
     */
    File getDirectory();

    /**
     * The command to that is being executed by the process
     * @return String command
     */
    String getCommand();

    /**
     * The arguments passed to the process
     * @return String args as List
     */
    List<String> getArguments();

    /**
     * The configured environment for the process.
     * @return Map of key/value String pairs for environment
     */
    Map<String, String> getEnvironment();

    /**
     * The current execution state of the process.
     * @return execution state
     */
    ExecHandleState getState();

    /**
     * Waits for the process to finish.
     *
     * @return result
     */
    ExecResult waitForFinish();

    /**
     * Returns if the exit value of the process should be ignored or not.
     * @return true if the exit value should be ignored. False otherwise.
     */
    boolean isIgnoreExitValue();

    /**
     * Abort the process
     */
    void abort();

}
