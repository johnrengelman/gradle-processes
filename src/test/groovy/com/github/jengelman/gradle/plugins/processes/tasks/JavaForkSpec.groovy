package com.github.jengelman.gradle.plugins.processes.tasks

import com.github.jengelman.gradle.plugins.processes.ProcessesPlugin
import com.github.jengelman.gradle.plugins.processes.util.TestFile
import com.github.jengelman.gradle.plugins.processes.util.TestMain
import com.github.jengelman.gradle.plugins.processes.util.TestNameTestDirectoryProvider
import org.gradle.internal.classloader.ClasspathUtil
import org.gradle.testkit.functional.ExecutionResult
import org.gradle.testkit.functional.GradleRunner
import org.gradle.testkit.functional.GradleRunnerFactory
import org.junit.Rule
import spock.lang.Specification

class JavaForkSpec extends Specification {

    @Rule
    final TestNameTestDirectoryProvider tmpDir = new TestNameTestDirectoryProvider()
    GradleRunner runner = GradleRunnerFactory.create()

    def setup() {
        buildFile << """
        apply plugin: ${ProcessesPlugin.name}
        """
        runner.directory = tmpDir.testDirectory
    }

    @SuppressWarnings(['Println', 'ClosureAsLastMethodParameter'])
    def forkTask() {
        given:
        File testFile = tmpDir.file('someFile')
        List<URL> files = ClasspathUtil.getClasspath(this.class.classLoader)
        buildFile << """
        List cp = ${files.collect { 'new URL("' + it.toString() + '")' } }
        task javaForkMain(type: ${JavaFork.name}) {
            classpath(cp as Object[])
            main = '${TestMain.name}'
            args '${testFile.absolutePath}'
        }

        task waitForFinish() {
            doLast {
                javaForkMain.processHandle.waitForFinish().assertNormalExitValue()
                println 'Process completed'
            }
        }

        javaForkMain.finalizedBy waitForFinish
        """

        when:
        println buildFile.text
        runner.arguments << 'javaForkMain'
        runner.arguments << '--stacktrace'
        ExecutionResult result = runner.run()

        then:
        assert result.standardOutput.contains('Process completed')
    }

    private TestFile getBuildFile() {
        tmpDir.testDirectory.file('build.gradle')
    }
}
