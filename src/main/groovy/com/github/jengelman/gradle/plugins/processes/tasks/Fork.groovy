package com.github.jengelman.gradle.plugins.processes.tasks

import com.github.jengelman.gradle.plugins.processes.ProcessHandle
import com.github.jengelman.gradle.plugins.processes.ProcessHandleListener
import com.github.jengelman.gradle.plugins.processes.ProcessesExtension
import com.github.jengelman.gradle.plugins.processes.internal.ForkAction
import org.gradle.api.internal.ConventionTask
import org.gradle.api.tasks.TaskAction
import org.gradle.process.ExecSpec
import org.gradle.process.ProcessForkOptions

/**
 * Gradle task that creates a Forked process and provides an access point for capturing the process handle in
 * subsequent tasks
 */
@SuppressWarnings('MethodCount')
class Fork extends ConventionTask implements ExecSpec {

    private ForkAction forkAction
    private ProcessHandle processHandle

    public Fork() {
        forkAction = ((ProcessesExtension) project.procs).newForkAction()
    }

    @TaskAction
    void fork() {
        processHandle = forkAction.fork()
    }

    @Override
    void setCommandLine(Object... objects) {
        forkAction.setCommandLine(objects)
    }

    @Override
    void setCommandLine(Iterable<?> objects) {
        forkAction.setCommandLine(objects)
    }

    @Override
    Fork commandLine(Object... objects) {
        forkAction.commandLine(objects)
        return this
    }

    @Override
    Fork commandLine(Iterable<?> objects) {
        forkAction.commandLine(objects)
        return this
    }

    @Override
    Fork args(Object... objects) {
        forkAction.args(objects)
        return this
    }

    @Override
    Fork args(Iterable<?> objects) {
        forkAction.args(objects)
        return this
    }

    @Override
    Fork setArgs(Iterable<?> objects) {
        forkAction.setArgs(objects)
        return this
    }

    @Override
    List<String> getArgs() {
        return forkAction.args
    }

    @Override
    Fork setIgnoreExitValue(boolean b) {
        forkAction.setIgnoreExitValue(b)
        return this
    }

    @Override
    boolean isIgnoreExitValue() {
        return forkAction.ignoreExitValue
    }

    @Override
    Fork setStandardInput(InputStream inputStream) {
        forkAction.setStandardInput(inputStream)
        return this
    }

    @Override
    InputStream getStandardInput() {
        return forkAction.standardInput
    }

    @Override
    Fork setStandardOutput(OutputStream outputStream) {
        forkAction.setStandardOutput(outputStream)
        return this
    }

    @Override
    OutputStream getStandardOutput() {
        return forkAction.standardOutput
    }

    @Override
    Fork setErrorOutput(OutputStream outputStream) {
        forkAction.setErrorOutput(outputStream)
        return this
    }

    @Override
    OutputStream getErrorOutput() {
        return forkAction.errorOutput
    }

    @Override
    List<String> getCommandLine() {
        return forkAction.commandLine
    }

    @Override
    String getExecutable() {
        return forkAction.executable
    }

    @Override
    void setExecutable(Object o) {
        forkAction.setExecutable(o)
    }

    @Override
    Fork executable(Object o) {
        forkAction.executable(o)
        return this
    }

    @Override
    File getWorkingDir() {
        return forkAction.workingDir
    }

    @Override
    void setWorkingDir(Object o) {
        forkAction.setWorkingDir(o)
    }

    @Override
    Fork workingDir(Object o) {
        forkAction.workingDir(o)
        return this
    }

    @Override
    Map<String, Object> getEnvironment() {
        return forkAction.environment
    }

    @Override
    void setEnvironment(Map<String, ?> stringMap) {
        forkAction.setEnvironment(stringMap)
    }

    @Override
    Fork environment(Map<String, ?> stringMap) {
        forkAction.environment(stringMap)
        return this
    }

    @Override
    Fork environment(String s, Object o) {
        forkAction.environment(s, o)
        return this
    }

    @Override
    Fork copyTo(ProcessForkOptions processForkOptions) {
        forkAction.copyTo(processForkOptions)
        return this
    }

    void setForkAction(ForkAction action) {
        this.forkAction = action
    }

    ProcessHandle getProcessHandle() {
        return this.processHandle
    }

    Fork listener(ProcessHandleListener listener) {
        forkAction.listener(listener)
        return this
    }
}
