package com.github.jengelman.gradle.plugins.processes.tasks

import com.github.jengelman.gradle.plugins.processes.ProcessHandle
import com.github.jengelman.gradle.plugins.processes.ProcessHandleListener
import com.github.jengelman.gradle.plugins.processes.ProcessesExtension
import com.github.jengelman.gradle.plugins.processes.internal.JavaForkAction
import org.gradle.api.file.FileCollection
import org.gradle.api.internal.ConventionTask
import org.gradle.api.tasks.Console
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.TaskAction
import org.gradle.process.CommandLineArgumentProvider
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
    @Input
    String getMain() {
        return forkAction.main
    }

    @Override
    JavaFork setMain(String s) {
        forkAction.setMain(s)
        return this
    }

    @Override
    @Input
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
        forkAction.setArgs(list)
        return this
    }

    @Override
    JavaFork setArgs(Iterable<?> objects) {
        forkAction.setArgs(objects)
        return this
    }

    @Override
    List<CommandLineArgumentProvider> getArgumentProviders() {
        return forkAction.argumentProviders
    }

    @Override
    JavaFork classpath(Object... objects) {
        forkAction.classpath(objects)
        return this
    }

    @Override
    @InputFiles
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
    @Input
    boolean isIgnoreExitValue() {
        return forkAction.ignoreExitValue
    }

    @Override
    JavaFork setStandardInput(InputStream inputStream) {
        forkAction.setStandardInput(inputStream)
        return this
    }

    @Override
    @Console
    InputStream getStandardInput() {
        return forkAction.standardInput
    }

    @Override
    JavaFork setStandardOutput(OutputStream outputStream) {
        forkAction.setStandardOutput(outputStream)
        return this
    }

    @Override
    @Console
    OutputStream getStandardOutput() {
        return forkAction.standardOutput
    }

    @Override
    JavaFork setErrorOutput(OutputStream outputStream) {
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
    @Input
    String getDefaultCharacterEncoding() {
        return forkAction.defaultCharacterEncoding
    }

    @Override
    void setDefaultCharacterEncoding(String s) {
        forkAction.setDefaultCharacterEncoding(s)
    }

    @Override
    @Input
    String getMinHeapSize() {
        return forkAction.minHeapSize
    }

    @Override
    void setMinHeapSize(String s) {
        forkAction.setMinHeapSize(s)
    }

    @Override
    @Input
    String getMaxHeapSize() {
        return forkAction.maxHeapSize
    }

    @Override
    void setMaxHeapSize(String s) {
        forkAction.setMaxHeapSize(s)
    }

    @Override
    @Input
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
    List<CommandLineArgumentProvider> getJvmArgumentProviders() {
        return forkAction.jvmArgumentProviders
    }

    @Override
    @InputFiles
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
    @Input
    boolean getEnableAssertions() {
        return forkAction.enableAssertions
    }

    @Override
    void setEnableAssertions(boolean b) {
        forkAction.setEnableAssertions(b)
    }

    @Override
    @Console
    boolean getDebug() {
        return forkAction.debug
    }

    @Override
    void setDebug(boolean b) {
        forkAction.setDebug(b)
    }

    @Override
    @Input
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
    JavaFork executable(Object o) {
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
    JavaFork workingDir(Object o) {
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

    @Internal
    ProcessHandle getProcessHandle() {
        return this.processHandle
    }

    JavaFork listener(ProcessHandleListener listener) {
        forkAction.listener(listener)
        return this
    }
}
