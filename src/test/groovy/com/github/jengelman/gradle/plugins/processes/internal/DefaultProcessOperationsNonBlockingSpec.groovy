package com.github.jengelman.gradle.plugins.processes.internal

import com.github.jengelman.gradle.plugins.processes.ProcessHandle
import com.github.jengelman.gradle.plugins.processes.ProcessHandleListener
import com.github.jengelman.gradle.plugins.processes.util.TestFiles
import com.github.jengelman.gradle.plugins.processes.util.TestMain
import com.github.jengelman.gradle.plugins.processes.util.WaitTestMain
import org.gradle.api.internal.file.DefaultFileOperations
import org.gradle.internal.classloader.ClasspathUtil
import org.gradle.internal.reflect.DirectInstantiator
import org.gradle.internal.reflect.Instantiator
import org.gradle.process.ExecResult
import org.gradle.process.internal.ExecException
import org.gradle.process.internal.ExecHandleState
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification

class DefaultProcessOperationsNonBlockingSpec extends Specification {

    private final Instantiator instantiator = new DirectInstantiator()

    private DefaultProcessOperations processOperations
    
    @Rule TemporaryFolder tmpDir

    def setup() {
        processOperations = new DefaultProcessOperations(instantiator, resolver(), fileOps())
    }

    def javafork() {
        given:
        File testFile = tmpDir.newFile('someFile')
        List files = ClasspathUtil.getClasspath(this.class.classLoader)

        when:
        ProcessHandle process = processOperations.javafork {
            classpath(files as Object[])
            main = TestMain.name
            args testFile.absolutePath
        }

        then:
        process.state != null

        when:
        ExecResult result = process.waitForFinish()

        then:
        testFile.isFile()
        result.exitValue == 0
    }

    def javaforkWithNonZeroExitValueShouldThrowException() {
        when:
        ProcessHandle process = processOperations.javafork {
            main = 'org.gradle.UnknownMain'
        }

        then:
        assert process.state != null

        when:
        ExecResult result = process.waitForFinish()
        if (!process.isIgnoreExitValue()) {
            result.assertNormalExitValue()
        }

        then:
        thrown(ExecException)
    }

    def javaforkWithNonZeroExitValueAndIgnoreExitValueShouldNotThrowException() {
        when:
        ProcessHandle process = processOperations.javafork {
            main = 'org.gradle.UnknownMain'
            ignoreExitValue = true
        }

        then:
        process != null

        when:
        ExecResult result = process.waitForFinish()

        then:
        result.exitValue != 0
    }

    def fork() {
        given:
        File testFile = tmpDir.newFile('someFile')

        when:
        ProcessHandle process = processOperations.fork {
            executable = 'touch'
            workingDir = tmpDir.root
            args testFile.name
        }

        then:
        process.state != null

        when:
        ExecResult result = process.waitForFinish()

        then:
        testFile.isFile()
        result.exitValue == 0
    }

    def execWithNonZeroExitValueShouldThrowException() {
        when:
        ProcessHandle process = processOperations.fork {
            executable = 'touch'
            workingDir = tmpDir.root
            args tmpDir.root.name + '/nonExistingDir/someFile'
        }

        then:
        assert process.state != null

        when:
        ExecResult result = process.waitForFinish()
        if (!process.isIgnoreExitValue()) {
            result.assertNormalExitValue()
        }

        then:
        thrown(ExecException)
    }

    def execWithNonZeroExitValueAndIgnoreExitValueShouldNotThrowException() {
        when:
        ProcessHandle process = processOperations.fork {
            ignoreExitValue = true
            executable = 'touch'
            workingDir = tmpDir.root
            args tmpDir.root.name + '/nonExistingDir/someFile'
        }

        then:
        process != null

        when:
        ExecResult result = process.waitForFinish()

        then:
        result.exitValue != 0
    }

    def listenToFork() {
        given:
        File testFile = tmpDir.newFile('someFile')
        boolean startedCalled = false
        boolean finishedCalled = false

        when:
        ProcessHandle process = processOperations.fork {
            executable = 'touch'
            workingDir = tmpDir.root
            args testFile.name
            listener new ProcessHandleListener() {

                @Override
                void executionStarted(ProcessHandle processHandle) {
                    startedCalled = true
                }

                @Override
                void executionFinished(ProcessHandle processHandle, ExecResult execResult) {
                    finishedCalled = true
                }
            }
        }
        process.waitForFinish()

        then:
        assert startedCalled
        assert finishedCalled
    }

    def listenToJavaFork() {
        given:
        File testFile = tmpDir.newFile('someFile')
        List files = ClasspathUtil.getClasspath(this.class.classLoader)
        boolean startedCalled = false
        boolean finishedCalled = false

        when:
        ProcessHandle process = processOperations.javafork {
            classpath(files as Object[])
            main = TestMain.name
            args testFile.absolutePath
            listener new ProcessHandleListener() {

                @Override
                void executionStarted(ProcessHandle processHandle) {
                    startedCalled = true
                }

                @Override
                void executionFinished(ProcessHandle processHandle, ExecResult execResult) {
                    finishedCalled = true
                }
            }
        }
        process.waitForFinish()

        then:
        assert startedCalled
        assert finishedCalled
    }

    def abortProcess() {
        given:
        List files = ClasspathUtil.getClasspath(this.class.classLoader)

        when:
        ProcessHandle process = processOperations.javafork {
            classpath(files as Object[])
            main = WaitTestMain.name
        }

        then:
        process.state == ExecHandleState.STARTED

        when:
        process.abort()

        then:
        process.state == ExecHandleState.STARTED //TODO This doesn't get set to ABORTED by Gradle core
        process.waitForFinish().exitValue != 0
    }

    def resolver() {
        return TestFiles.resolver(tmpDir.root)
    }

    private DefaultFileOperations fileOps() {
        new DefaultFileOperations(resolver(), null, null, instantiator)
    }
}
