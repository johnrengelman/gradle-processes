package com.github.jengelman.gradle.plugins

import com.github.jengelman.gradle.plugins.processes.NonBlockingProcessApi
import com.github.jengelman.gradle.plugins.processes.ProcessHandle
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
        def ext = project.extensions.getByType(ProcessesPlugin)
        assert ext instanceof NonBlockingProcessApi
    }

    def 'fork a java process from the plugin extension'() {
        given:
        File testFile = tmpDir.file("someFile")
        List files = ClasspathUtil.getClasspath(getClass().classLoader)

        when:
        ProcessHandle process = project.proc.javafork {
            classpath(files as Object[])
            main = 'org.gradle.api.plugins.SomeMain'
            args testFile.absolutePath
        }

        then:
        process.state != null

        when:
        ExecResult result = project.proc.waitForFinish(process)

        then:
        testFile.isFile()
        result.exitValue == 0
    }

    def 'forked java process with ignored exit value should not throw exception'() {
        when:
        project.plugins.apply(ProcessesPlugin)
        ProcessHandle process = project.proc.javafork {
            main = 'org.gradle.UnknownMain'
            ignoreExitValue = true
        }

        then:
        assert process != null

        when:
        ExecResult result = project.proc.waitForFinish(process)

        then:
        assert result.exitValue != 0
    }

    def 'forked java process  should throw exception'() {
        when:
        ProcessHandle process = project.proc.javafork {
            main = 'org.gradle.UnknownMain'
        }

        then:
        assert process != null

        when:
        project.proc.waitForFinish(process)

        then:
        thrown(ExecException)
    }

    def 'wait for multiple forked java processes to complete'() {
        given:
        File testFile = tmpDir.file("someFile")
        File testFile2 = tmpDir.file("someFile2")
        List files = ClasspathUtil.getClasspath(getClass().classLoader)

        when:
        ProcessHandle process = project.proc.javafork {
            classpath(files as Object[])
            main = 'org.gradle.api.plugins.SomeMain'
            args testFile.absolutePath
        }
        ProcessHandle process2 = project.proc.javafork {
            classpath(files as Object[])
            main = 'org.gradle.api.plugins.SomeMain'
            args testFile2.absolutePath
        }

        then:
        assert process.state != null
        assert process2.state != null

        when:
        List<ExecResult> results = project.proc.waitForFinish([process, process2])

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
        ProcessHandle process = project.proc.javafork {
            classpath(files as Object[])
            main = 'org.gradle.api.plugins.SomeMain'
            args testFile.absolutePath
        }
        ProcessHandle process2 = project.proc.javafork {
            main = 'org.gradle.UnknownMain'
            ignoreExitValue = true
        }

        then:
        assert process.state != null
        assert process2.state != null

        when:
        List<ExecResult> results = project.proc.waitForFinish([process, process2])

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
