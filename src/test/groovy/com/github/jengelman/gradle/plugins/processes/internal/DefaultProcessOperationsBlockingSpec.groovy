package com.github.jengelman.gradle.plugins.processes.internal

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

class DefaultProcessOperationsBlockingSpec extends Specification {

    private final Instantiator instantiator = new DirectInstantiator()

    private DefaultProcessOperations processOperations

    @Rule
    public final TestNameTestDirectoryProvider tmpDir = new TestNameTestDirectoryProvider()

    def setup() {
        processOperations = new DefaultProcessOperations(instantiator, resolver(), fileOps())
    }

    def javaexec() {
        File testFile = tmpDir.file("someFile")
        List files = ClasspathUtil.getClasspath(getClass().classLoader)

        when:
        ExecResult result = processOperations.javaexec {
            classpath(files as Object[])
            main = TestMain.class.name
            args testFile.absolutePath
        }

        then:
        testFile.isFile()
        result.exitValue == 0
    }

    def javaexecWithNonZeroExitValueShouldThrowException() {
        when:
        processOperations.javaexec {
            main = 'org.gradle.UnknownMain'
        }

        then:
        thrown(ExecException)
    }

    def javaexeckWithNonZeroExitValueAndIgnoreExitValueShouldNotThrowException() {
        when:
        ExecResult result = processOperations.javaexec {
            main = 'org.gradle.UnknownMain'
            ignoreExitValue = true
        }

        then:
        result.exitValue != 0
    }

    def exec() {
        given:
        File testFile = tmpDir.file("someFile")

        when:
        ExecResult result = processOperations.exec {
            executable = "touch"
            workingDir = tmpDir.getTestDirectory()
            args testFile.name
        }

        then:
        testFile.isFile()
        result.exitValue == 0
    }

    def execWithNonZeroExitValueShouldThrowException() {
        when:
        processOperations.exec {
            executable = "touch"
            workingDir = tmpDir.getTestDirectory()
            args tmpDir.testDirectory.name + "/nonExistingDir/someFile"
        }

        then:
        thrown(ExecException)
    }

    def execWithNonZeroExitValueAndIgnoreExitValueShouldNotThrowException() {
        when:
        ExecResult result = processOperations.exec {
            ignoreExitValue = true
            executable = "touch"
            workingDir = tmpDir.getTestDirectory()
            args tmpDir.testDirectory.name + "/nonExistingDir/someFile"
        }

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
