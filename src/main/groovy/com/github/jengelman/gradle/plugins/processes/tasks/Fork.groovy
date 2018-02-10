package com.github.jengelman.gradle.plugins.processes.tasks

import com.github.jengelman.gradle.plugins.processes.ProcessHandle
import com.github.jengelman.gradle.plugins.processes.ProcessHandleListener
import com.github.jengelman.gradle.plugins.processes.ProcessesExtension
import com.github.jengelman.gradle.plugins.processes.internal.ForkAction
import org.gradle.api.internal.ConventionTask
import org.gradle.api.tasks.Console
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.Internal
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
    void setCommandLine(List<String> list) {
        forkAction.setCommandLine(list)
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
    Fork setArgs(List<String> list) {
        forkAction.setArgs(list)
        return this
    }

    @Override
    Fork setArgs(Iterable<?> objects) {
        forkAction.setArgs(objects)
        return this
    }

    @Override
    @Input
    List<String> getArgs() {
        return forkAction.args
    }

    @Override
    Fork setIgnoreExitValue(boolean b) {
        forkAction.setIgnoreExitValue(b)
        return this
    }

    @Override
    @Input
    boolean isIgnoreExitValue() {
        return forkAction.ignoreExitValue
    }

    @Override
    Fork setStandardInput(InputStream inputStream) {
        forkAction.setStandardInput(inputStream)
        return this
    }

    @Override
    @Console
    InputStream getStandardInput() {
        return forkAction.standardInput
    }

    @Override
    Fork setStandardOutput(OutputStream outputStream) {
        forkAction.setStandardOutput(outputStream)
        return this
    }

    @Override
    @Console
    OutputStream getStandardOutput() {
        return forkAction.standardOutput
    }

    @Override
    Fork setErrorOutput(OutputStream outputStream) {
        forkAction.setErrorOutput(outputStream)
        return this
    }

    @Override
    @Console
    OutputStream getErrorOutput() {
        return forkAction.errorOutput
    }

    @Override
    @Input
    List<String> getCommandLine() {
        return forkAction.commandLine
    }

    @Override
    @Input
    String getExecutable() {
        return forkAction.executable
    }

    @Override
    void setExecutable(String s) {
        forkAction.setExecutable(s)
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
    @InputDirectory
    File getWorkingDir() {
        return forkAction.workingDir
    }

    @Override
    void setWorkingDir(File file) {
        forkAction.setWorkingDir(file)
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
    @Input
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

    @Internal
    ProcessHandle getProcessHandle() {
        return this.processHandle
    }

    Fork listener(ProcessHandleListener listener) {
        forkAction.listener(listener)
        return this
    }
}
