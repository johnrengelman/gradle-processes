package com.github.jengelman.gradle.plugins.processes.internal

import com.github.jengelman.gradle.plugins.processes.util.TestFiles
import com.github.jengelman.gradle.plugins.processes.util.TestMain
import org.gradle.api.internal.file.DefaultFileOperations
import org.gradle.internal.classloader.ClasspathUtil
import org.gradle.internal.reflect.DirectInstantiator
import org.gradle.internal.reflect.Instantiator
import org.gradle.process.ExecResult
import org.gradle.process.internal.ExecException
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification

class DefaultProcessOperationsBlockingSpec extends Specification {

    private final Instantiator instantiator = new DirectInstantiator()

    private DefaultProcessOperations processOperations

    @Rule TemporaryFolder tmpDir

    def setup() {
        processOperations = new DefaultProcessOperations(instantiator, resolver(), fileOps())
    }

    def javaexec() {
        File testFile = tmpDir.newFile('someFile')
        List files = ClasspathUtil.getClasspath(this.class.classLoader)

        when:
        ExecResult result = processOperations.javaexec {
            classpath(files as Object[])
            main = TestMain.name
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
        File testFile = tmpDir.newFile('someFile')

        when:
        ExecResult result = processOperations.exec {
            executable = 'touch'
            workingDir = tmpDir.root
            args testFile.name
        }

        then:
        testFile.isFile()
        result.exitValue == 0
    }

    def execWithNonZeroExitValueShouldThrowException() {
        when:
        processOperations.exec {
            executable = 'touch'
            workingDir = tmpDir.root
            args tmpDir.root.name + '/nonExistingDir/someFile'
        }

        then:
        thrown(ExecException)
    }

    def execWithNonZeroExitValueAndIgnoreExitValueShouldNotThrowException() {
        when:
        ExecResult result = processOperations.exec {
            ignoreExitValue = true
            executable = 'touch'
            workingDir = tmpDir.root
            args tmpDir.root.name + '/nonExistingDir/someFile'
        }

        then:
        result.exitValue != 0
    }

    def resolver() {
        return TestFiles.resolver(tmpDir.root)
    }

    private DefaultFileOperations fileOps() {
        new DefaultFileOperations(resolver(), null, null, instantiator)
    }
}
