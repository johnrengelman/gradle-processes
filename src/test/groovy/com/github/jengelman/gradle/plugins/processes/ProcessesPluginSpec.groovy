package com.github.jengelman.gradle.plugins.processes

import com.github.jengelman.gradle.plugins.processes.internal.NonBlockingProcessOperations
import com.github.jengelman.gradle.plugins.processes.util.TestMain
import org.gradle.api.Project
import org.gradle.internal.classloader.ClasspathUtil
import org.gradle.process.ExecResult
import org.gradle.process.internal.ExecException
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification

class ProcessesPluginSpec extends Specification {

    private Project project

    @Rule TemporaryFolder tmpDir

    def setup() {
        project = ProjectBuilder.builder().build()
        project.plugins.apply(ProcessesPlugin)
    }

    def 'apply plugin with name'() {
        given:
        project = ProjectBuilder.builder().build()

        when:
        project.plugins.apply('com.github.johnrengelman.processes')

        then:
        assert project.plugins.hasPlugin(ProcessesPlugin)
    }

    def 'plugin creates extension'() {
        expect:
        assert project.plugins.hasPlugin(ProcessesPlugin)
        assert project.extensions.getByType(ProcessesExtension)
        assert project.extensions.findByName(ProcessesPlugin.PROCESSES_EXTENSION)
    }

    def 'extension implements async operations interface'() {
        expect:
        def ext = project.extensions.getByType(ProcessesExtension)
        assert ext instanceof NonBlockingProcessOperations
    }

    def 'fork a java process from the plugin extension'() {
        given:
        File testFile = tmpDir.newFile('someFile')
        List files = ClasspathUtil.getClasspath(this.class.classLoader)

        when:
        ProcessHandle process = project.procs.javafork {
            classpath(files as Object[])
            main = TestMain.name
            args testFile.absolutePath
        }

        then:
        process.state != null

        when:
        ExecResult result = project.procs.waitForFinish(process)

        then:
        testFile.isFile()
        result.exitValue == 0
    }

    def 'exec a java process from the plugin extension'() {
        given:
        File testFile = tmpDir.newFile('someFile')
        List files = ClasspathUtil.getClasspath(this.class.classLoader)

        when:
        ExecResult result = project.procs.javaexec {
            classpath(files as Object[])
            main = TestMain.name
            args testFile.absolutePath
        }

        then:
        testFile.isFile()
        result.exitValue == 0
    }

    def 'fork a process from the plugin extension'() {
        given:
        File testFile = tmpDir.newFile('someFile')

        when:
        ProcessHandle process = project.procs.fork {
            executable = 'touch'
            workingDir = tmpDir.root
            args testFile.name
        }

        then:
        process.state != null

        when:
        ExecResult result = project.procs.waitForFinish(process)

        then:
        testFile.isFile()
        result.exitValue == 0
    }

    def 'exec a process from the plugin extension'() {
        given:
        File testFile = tmpDir.newFile('someFile')

        when:
        ExecResult result = project.procs.exec {
            executable = 'touch'
            workingDir = tmpDir.root
            args testFile.name
        }

        then:
        testFile.isFile()
        result.exitValue == 0
    }

    def 'forked java process with ignored exit value should not throw exception'() {
        when:
        project.plugins.apply(ProcessesPlugin)
        ProcessHandle process = project.procs.javafork {
            main = 'org.gradle.UnknownMain'
            ignoreExitValue = true
        }

        then:
        assert process != null

        when:
        ExecResult result = project.procs.waitForFinish(process)

        then:
        assert result.exitValue != 0
    }

    def 'forked java process should throw exception'() {
        when:
        ProcessHandle process = project.procs.javafork {
            main = 'org.gradle.UnknownMain'
        }

        then:
        assert process != null

        when:
        project.procs.waitForFinish(process)

        then:
        thrown(ExecException)
    }

    def 'wait for multiple forked java processes to complete'() {
        given:
        File testFile = tmpDir.newFile('someFile')
        File testFile2 = tmpDir.newFile('someFile2')
        List files = ClasspathUtil.getClasspath(this.class.classLoader)

        when:
        ProcessHandle process = project.procs.javafork {
            classpath(files as Object[])
            main = TestMain.name
            args testFile.absolutePath
        }
        ProcessHandle process2 = project.procs.javafork {
            classpath(files as Object[])
            main = TestMain.name
            args testFile2.absolutePath
        }

        then:
        assert process.state != null
        assert process2.state != null

        when:
        List<ExecResult> results = project.procs.waitForFinish([process, process2])

        then:
        assert testFile.isFile()
        assert testFile2.isFile()
        results.each {
            assert it.exitValue == 0
        }
    }

    def 'wait for multiple forked java processes to complete with ignored exit should not throw exception'() {
        given:
        File testFile = tmpDir.newFile('someFile')
        List files = ClasspathUtil.getClasspath(this.class.classLoader)

        when:
        ProcessHandle process = project.procs.javafork {
            classpath(files as Object[])
            main = TestMain.name
            args testFile.absolutePath
        }
        ProcessHandle process2 = project.procs.javafork {
            main = 'org.gradle.UnknownMain'
            ignoreExitValue = true
        }

        then:
        assert process.state != null
        assert process2.state != null

        when:
        List<ExecResult> results = project.procs.waitForFinish([process, process2])

        then:
        assert testFile.isFile()
        assert results[0].exitValue == 0
        assert results[1].exitValue != 0
    }
}
