package com.github.jengelman.gradle.plugins.processes.internal

import com.github.jengelman.gradle.plugins.processes.ProcessHandle
import com.github.jengelman.gradle.plugins.processes.TestFiles
import com.github.jengelman.gradle.plugins.processes.TestMain
import com.github.jengelman.gradle.plugins.processes.TestNameTestDirectoryProvider
import org.gradle.api.internal.file.DefaultFileOperations
import org.gradle.internal.classloader.ClasspathUtil
import org.gradle.internal.reflect.DirectInstantiator
import org.gradle.internal.reflect.Instantiator
import org.gradle.process.ExecResult
import org.gradle.process.internal.ExecException
import org.junit.Rule
import spock.lang.Specification

class DefaultProcessOperationsNonBlockingSpec extends Specification {

    private final Instantiator instantiator = new DirectInstantiator()

    private DefaultProcessOperations processOperations
    
    @Rule
    public final TestNameTestDirectoryProvider tmpDir = new TestNameTestDirectoryProvider()

    def setup() {
        processOperations = new DefaultProcessOperations(instantiator, resolver(), fileOps())
    }

    def javafork() {
        given:
        File testFile = tmpDir.file("someFile")
        List files = ClasspathUtil.getClasspath(getClass().classLoader)

        when:
        ProcessHandle process = processOperations.javafork {
            classpath(files as Object[])
            main = TestMain.class.name
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
        File testFile = tmpDir.file("someFile")

        when:
        ProcessHandle process = processOperations.fork {
            executable = "touch"
            workingDir = tmpDir.getTestDirectory()
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
            executable = "touch"
            workingDir = tmpDir.getTestDirectory()
            args tmpDir.testDirectory.name + "/nonExistingDir/someFile"
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
            executable = "touch"
            workingDir = tmpDir.getTestDirectory()
            args tmpDir.testDirectory.name + "/nonExistingDir/someFile"
        }

        then:
        process != null

        when:
        ExecResult result = process.waitForFinish()

        then:
        result.exitValue != 0
    }

    def resolver() {
        return TestFiles.resolver(tmpDir.testDirectory)
    }

    private DefaultFileOperations fileOps() {
        new DefaultFileOperations(resolver(), null, null, instantiator)
    }
}
