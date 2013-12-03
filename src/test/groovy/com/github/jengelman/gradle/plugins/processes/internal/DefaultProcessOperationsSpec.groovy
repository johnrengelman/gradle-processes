package com.github.jengelman.gradle.plugins.processes.internal

import com.github.jengelman.gradle.plugins.processes.ProcessHandle
import com.github.jengelman.gradle.plugins.processes.TestFiles
import com.github.jengelman.gradle.plugins.processes.TestMain
import com.github.jengelman.gradle.plugins.processes.TestNameTestDirectoryProvider
import org.gradle.internal.classloader.ClasspathUtil
import org.gradle.internal.reflect.DirectInstantiator
import org.gradle.internal.reflect.Instantiator
import org.gradle.process.ExecResult
import org.gradle.process.internal.ExecException
import org.junit.Rule
import spock.lang.Specification

class DefaultProcessOperationsSpec extends Specification {

    private final Instantiator instantiator = new DirectInstantiator()

    private DefaultProcessOperations processOperations
    
    @Rule
    public final TestNameTestDirectoryProvider tmpDir = new TestNameTestDirectoryProvider()

    def javafork() {
        File testFile = tmpDir.file("someFile")
        processOperations = new DefaultProcessOperations(instantiator, resolver())
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
        processOperations = new DefaultProcessOperations(instantiator, resolver())

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
        processOperations = new DefaultProcessOperations(instantiator, resolver())

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

    def resolver() {
        return TestFiles.resolver(tmpDir.testDirectory)
    }
}
