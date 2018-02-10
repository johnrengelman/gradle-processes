package com.github.jengelman.gradle.plugins.processes.internal

import com.github.jengelman.gradle.plugins.processes.ProcessHandle
import com.github.jengelman.gradle.plugins.processes.ProcessHandleListener
import com.github.jengelman.gradle.plugins.processes.util.TestFiles
import com.github.jengelman.gradle.plugins.processes.util.TestMain
import com.github.jengelman.gradle.plugins.processes.util.WaitTestMain
import org.gradle.internal.classloader.ClasspathUtil
import org.gradle.internal.reflect.DirectInstantiator
import org.gradle.process.ExecResult
import org.gradle.process.internal.ExecException
import org.gradle.process.internal.ExecHandleState
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification

class DefaultProcessOperationsNonBlockingSpec extends Specification {

    private DefaultProcessOperations processOperations
    
    @Rule TemporaryFolder tmpDir

    def setup() {
        processOperations = new DefaultProcessOperations(DirectInstantiator.INSTANCE, TestFiles.resolver(tmpDir.root), TestFiles.fileOperations(tmpDir.root))
    }

    def "java fork should be able to touch a file"() {
        given:
        File testFile = tmpDir.newFile('someFile')
        List<File> files = ClasspathUtil.getClasspath(this.class.classLoader).asFiles

        when:
        ProcessHandle process = processOperations.javafork {
            classpath(files)
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

    def "java fork with non-zero exit result should throw an exception"() {
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

    def "java fork with non-zero exit result and ignored exit value should NOT throw an exception"() {
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

    def "fork should be able to touch a file"() {
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

    def  "fork with non-zero exit result should throw an exception"() {
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

    def "fork with non-zero exit result and ignored exit value should NOT throw an exception"() {
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

    def "fork listener should be able to listen to forked process"() {
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

    def "java fork listener should be able to listen to a forked java process"() {
        given:
        File testFile = tmpDir.newFile('someFile')
        List<File> files = ClasspathUtil.getClasspath(this.class.classLoader).asFiles
        boolean startedCalled = false
        boolean finishedCalled = false

        when:
        ProcessHandle process = processOperations.javafork {
            classpath(files)
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

    def "it should be possible to abort a stuck process"() {
        given:
        List<File> files = ClasspathUtil.getClasspath(this.class.classLoader).asFiles

        when:
        ProcessHandle process = processOperations.javafork {
            classpath(files)
            main = WaitTestMain.name
        }

        then:
        process.state == ExecHandleState.STARTED

        when:
        process.abort()

        then:
        process.state == ExecHandleState.ABORTED
        process.waitForFinish().exitValue != 0
    }

}
