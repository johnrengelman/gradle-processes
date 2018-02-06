package com.github.jengelman.gradle.plugins.processes.tasks

import com.github.jengelman.gradle.plugins.processes.ProcessHandle
import com.github.jengelman.gradle.plugins.processes.ProcessHandleListener
import com.github.jengelman.gradle.plugins.processes.ProcessesExtension
import com.github.jengelman.gradle.plugins.processes.internal.ForkAction
import com.github.jengelman.gradle.plugins.processes.internal.JavaForkAction
import org.gradle.api.file.FileCollection
import org.gradle.api.internal.ConventionTask
import org.gradle.api.tasks.TaskAction
import org.gradle.process.JavaExecSpec
import org.gradle.process.JavaForkOptions
import org.gradle.process.ProcessForkOptions

/**
 * Gradle task that creates a Forked Java process and provides an access point for capturing the process handle in
 * subsequent tasks
 */
@SuppressWarnings('MethodCount')
class JavaFork extends ConventionTask implements JavaExecSpec {

    private JavaForkAction forkAction
    private ProcessHandle processHandle

    JavaFork() {
        forkAction = ((ProcessesExtension) project.procs).newJavaForkAction()
    }

    @TaskAction
    void javafork() {
        setMain(main) // make convention mapping work (at least for 'main'...
        setJvmArgs(jvmArgs) // ...and for 'jvmArgs')
        processHandle = forkAction.fork()
    }

    @Override
    String getMain() {
        return forkAction.main
    }

    @Override
    JavaFork setMain(String s) {
        forkAction.setMain(s)
        return this
    }

    @Override
    List<String> getArgs() {
        return forkAction.args
    }

    @Override
    JavaFork args(Object... objects) {
        forkAction.args(objects)
        return this
    }

    @Override
    JavaFork args(Iterable<?> objects) {
        forkAction.args(objects)
        return this
    }

    @Override
    JavaFork setArgs(List<String> list) {
        return forkAction.setArgs(list)
    }

    @Override
    JavaFork setArgs(Iterable<?> objects) {
        forkAction.setArgs(objects)
        return this
    }

    @Override
    JavaFork classpath(Object... objects) {
        forkAction.classpath(objects)
        return this
    }

    @Override
    FileCollection getClasspath() {
        return forkAction.classpath
    }

    @Override
    JavaFork setClasspath(FileCollection files) {
        forkAction.setClasspath(files)
        return this
    }

    @Override
    JavaFork setIgnoreExitValue(boolean b) {
        forkAction.setIgnoreExitValue(b)
        return this
    }

    @Override
    boolean isIgnoreExitValue() {
        return forkAction.ignoreExitValue
    }

    @Override
    JavaFork setStandardInput(InputStream inputStream) {
        forkAction.setStandardInput(inputStream)
        return this
    }

    @Override
    InputStream getStandardInput() {
        return forkAction.standardInput
    }

    @Override
    JavaFork setStandardOutput(OutputStream outputStream) {
        forkAction.setStandardOutput(outputStream)
        return this
    }

    @Override
    OutputStream getStandardOutput() {
        return forkAction.standardOutput
    }

    @Override
    JavaFork setErrorOutput(OutputStream outputStream) {
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
    Map<String, Object> getSystemProperties() {
        return forkAction.systemProperties
    }

    @Override
    void setSystemProperties(Map<String, ?> stringMap) {
        forkAction.setSystemProperties(stringMap)
    }

    @Override
    JavaFork systemProperties(Map<String, ?> stringMap) {
        forkAction.systemProperties(stringMap)
        return this
    }

    @Override
    JavaFork systemProperty(String s, Object o) {
        forkAction.systemProperty(s, o)
        return this
    }

    @Override
    String getDefaultCharacterEncoding() {
        return forkAction.defaultCharacterEncoding
    }

    @Override
    void setDefaultCharacterEncoding(String s) {
        forkAction.setDefaultCharacterEncoding(s)
    }

    @Override
    String getMinHeapSize() {
        return forkAction.minHeapSize
    }

    @Override
    void setMinHeapSize(String s) {
        forkAction.setMinHeapSize(s)
    }

    @Override
    String getMaxHeapSize() {
        return forkAction.maxHeapSize
    }

    @Override
    void setMaxHeapSize(String s) {
        forkAction.setMaxHeapSize(s)
    }

    @Override
    List<String> getJvmArgs() {
        return forkAction.jvmArgs
    }

    @Override
    void setJvmArgs(List<String> list) {
        forkAction.setJvmArgs(list)
    }

    @Override
    void setJvmArgs(Iterable<?> objects) {
        forkAction.setJvmArgs(objects)
    }

    @Override
    JavaFork jvmArgs(Iterable<?> objects) {
        forkAction.jvmArgs(objects)
        return this
    }

    @Override
    JavaFork jvmArgs(Object... objects) {
        forkAction.jvmArgs(objects)
        return this
    }

    @Override
    FileCollection getBootstrapClasspath() {
        return forkAction.bootstrapClasspath
    }

    @Override
    void setBootstrapClasspath(FileCollection files) {
        forkAction.setBootstrapClasspath(files)
    }

    @Override
    JavaFork bootstrapClasspath(Object... objects) {
        forkAction.bootstrapClasspath(objects)
        return this
    }

    @Override
    boolean getEnableAssertions() {
        return forkAction.enableAssertions
    }

    @Override
    void setEnableAssertions(boolean b) {
        forkAction.setEnableAssertions(b)
    }

    @Override
    boolean getDebug() {
        return forkAction.debug
    }

    @Override
    void setDebug(boolean b) {
        forkAction.setDebug(b)
    }

    @Override
    List<String> getAllJvmArgs() {
        return forkAction.allJvmArgs
    }

    @Override
    void setAllJvmArgs(List<String> list) {
        forkAction.setAllJvmArgs(list)
    }

    @Override
    void setAllJvmArgs(Iterable<?> objects) {
        forkAction.setAllJvmArgs(objects)
    }

    @Override
    JavaFork copyTo(JavaForkOptions javaForkOptions) {
        forkAction.copyTo(javaForkOptions)
        return this
    }

    @Override
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
    JavaFork executable(Object o) {
        forkAction.executable(o)
        return this
    }

    @Override
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
    JavaFork workingDir(Object o) {
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
    JavaFork environment(Map<String, ?> stringMap) {
        forkAction.environment(stringMap)
        return this
    }

    @Override
    JavaFork environment(String s, Object o) {
        forkAction.environment(s, o)
        return this
    }

    @Override
    JavaFork copyTo(ProcessForkOptions processForkOptions) {
        forkAction.copyTo(processForkOptions)
        return this
    }

    void setForkAction(JavaForkAction action) {
        this.forkAction = action
    }


    ProcessHandle getProcessHandle() {
        return this.processHandle
    }

    JavaFork listener(ProcessHandleListener listener) {
        forkAction.listener(listener)
        return this
    }
}
