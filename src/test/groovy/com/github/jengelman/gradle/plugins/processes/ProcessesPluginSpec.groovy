package com.github.jengelman.gradle.plugins.processes

import com.github.jengelman.gradle.plugins.processes.internal.NonBlockingProcessApi
import org.apache.commons.io.FileUtils
import org.gradle.api.Project
import org.gradle.internal.classloader.ClasspathUtil
import org.gradle.process.ExecResult
import org.gradle.process.internal.ExecException
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Rule
import spock.lang.Specification

class ProcessesPluginSpec extends Specification {

    private Project project

    @Rule
    public final TestNameTestDirectoryProvider tmpDir = new TestNameTestDirectoryProvider()

    def setup() {
        project = ProjectBuilder.builder().build()
        project.plugins.apply(ProcessesPlugin)
    }

    def "creates extension"() {
        expect:
        assert project.plugins.hasPlugin(ProcessesPlugin)
        assert project.extensions.getByType(ProcessesExtension)
        assert project.extensions.findByName(ProcessesPlugin.PROCESSES_EXTENSION)
    }

    def 'extension implements async operations interface'() {
        expect:
        def ext = project.extensions.getByType(ProcessesExtension)
        assert ext instanceof NonBlockingProcessApi
    }

    def 'fork a java process from the plugin extension'() {
        given:
        File testFile = tmpDir.file("someFile")
        List files = ClasspathUtil.getClasspath(getClass().classLoader)

        when:
        ProcessHandle process = project.procs.javafork {
            classpath(files as Object[])
            main = 'com.github.jengelman.gradle.plugins.processes.SomeMain'
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

    def 'forked java process  should throw exception'() {
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
        File testFile = tmpDir.file("someFile")
        File testFile2 = tmpDir.file("someFile2")
        List files = ClasspathUtil.getClasspath(getClass().classLoader)

        when:
        ProcessHandle process = project.procs.javafork {
            classpath(files as Object[])
            main = 'com.github.jengelman.gradle.plugins.processes.SomeMain'
            args testFile.absolutePath
        }
        ProcessHandle process2 = project.procs.javafork {
            classpath(files as Object[])
            main = 'com.github.jengelman.gradle.plugins.processes.SomeMain'
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
        File testFile = tmpDir.file("someFile")
        List files = ClasspathUtil.getClasspath(getClass().classLoader)

        when:
        ProcessHandle process = project.procs.javafork {
            classpath(files as Object[])
            main = 'com.github.jengelman.gradle.plugins.processes.SomeMain'
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

class SomeMain {
    static void main(String[] args) {
        FileUtils.touch(new File(args[0]))
    }
}
