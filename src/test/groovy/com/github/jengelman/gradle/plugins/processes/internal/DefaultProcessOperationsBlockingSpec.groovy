package com.github.jengelman.gradle.plugins.processes.internal

import com.github.jengelman.gradle.plugins.processes.util.TestFiles
import com.github.jengelman.gradle.plugins.processes.util.TestMain
import org.gradle.internal.classloader.ClasspathUtil
import org.gradle.internal.nativeintegration.services.NativeServices
import org.gradle.internal.reflect.DirectInstantiator
import org.gradle.process.ExecResult
import org.gradle.process.internal.ExecException
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification

class DefaultProcessOperationsBlockingSpec extends Specification {


    private DefaultProcessOperations processOperations

    @Rule
    TemporaryFolder tmpDir

    def setup() {
        NativeServices.initialize(tmpDir.root)
        processOperations = new DefaultProcessOperations(DirectInstantiator.INSTANCE, TestFiles.resolver(tmpDir.root), TestFiles.fileOperations(tmpDir.root))
    }


    def "javaexec task should be able to touch a file"() {
        File testFile = tmpDir.newFile('someFile')
        List<File> files = ClasspathUtil.getClasspath(this.class.classLoader).asFiles

        when:
        ExecResult result = processOperations.javaexec { spec ->
            spec.with {
                classpath(files)
                main = TestMain.name
                args testFile.absolutePath
            }
        }

        then:
        testFile.isFile()
        result.exitValue == 0
    }

    def "javaexec task with non-zero exitvalue should throw an exception"() {
        when:
        processOperations.javaexec { spec ->
            spec.main = 'org.gradle.UnknownMain'
        }

        then:
        thrown(ExecException)
    }

    def "javaexec task with non-zero exitvalue that ignores exit value should NOT throw an exception"() {
        when:
        ExecResult result = processOperations.javaexec { spec ->
            spec.with {
                main = 'org.gradle.UnknownMain'
                ignoreExitValue = true
            }
        }

        then:
        result.exitValue != 0
    }

    def "exec task should be able to touch a file"() {
        given:
        File testFile = tmpDir.newFile('someFile')

        when:
        ExecResult result = processOperations.exec { spec ->
            spec.with {
                executable = 'touch'
                workingDir = tmpDir.root
                args testFile.name
            }
        }

        then:
        testFile.isFile()
        result.exitValue == 0
    }

    def "exec task with non-zero exitvalue should throw an exception"() {
        when:
        processOperations.exec { spec ->
            spec.with {
                executable = 'touch'
                workingDir = tmpDir.root
                args tmpDir.root.name + '/nonExistingDir/someFile'
            }
        }

        then:
        thrown(ExecException)
    }

    def "exec task with non-zero exitvalue that ignores exit value should NOT throw an exception"() {
        when:
        ExecResult result = processOperations.exec { spec ->
            spec.with {
                ignoreExitValue = true
                executable = 'touch'
                workingDir = tmpDir.root
                args tmpDir.root.name + '/nonExistingDir/someFile'
            }
        }

        then:
        result.exitValue != 0
    }
}
