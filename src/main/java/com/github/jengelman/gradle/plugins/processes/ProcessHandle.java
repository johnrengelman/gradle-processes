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
     * @return
     */
    File getDirectory();

    /**
     * The command to that is being executed by the process
     * @return
     */
    String getCommand();

    /**
     * The arguments passed to the process
     * @return
     */
    List<String> getArguments();

    /**
     * The configured environment for the process.
     * @return
     */
    Map<String, String> getEnvironment();

    /**
     * The current execution state of the process.
     * @return
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
}
