package com.github.jengelman.gradle.plugins.processes.tasks

import com.github.jengelman.gradle.plugins.processes.ProcessesPlugin
import com.github.jengelman.gradle.plugins.processes.util.TestFile
import com.github.jengelman.gradle.plugins.processes.util.TestNameTestDirectoryProvider
import org.gradle.testkit.functional.ExecutionResult
import org.gradle.testkit.functional.GradleRunner
import org.gradle.testkit.functional.GradleRunnerFactory
import org.junit.Rule
import spock.lang.Specification

class ForkSpec extends Specification {

    @Rule
    final TestNameTestDirectoryProvider tmpDir = new TestNameTestDirectoryProvider()
    GradleRunner runner = GradleRunnerFactory.create()

    def setup() {
        buildFile << """
        apply plugin: ${ProcessesPlugin.name}
        """
        runner.directory = tmpDir.testDirectory
    }

    @SuppressWarnings('Println')
    def forkTask() {
        given:
        File testFile = tmpDir.testDirectory.file('someFile')

        buildFile << """
        task forkMain(type: ${Fork.name}) {
            executable = 'touch'
            workingDir = "${tmpDir.testDirectory}"
            args "${testFile.path}"
        }

        task waitForFinish() {
            doLast {
                def result = forkMain.processHandle.waitForFinish()
                println 'Result is: ' + result.exitValue
            }
        }

        forkMain.finalizedBy waitForFinish
        """

        when:
        runner.arguments << 'forkMain'
        ExecutionResult result = runner.run()

        then:
        println result.standardOutput
        println result.standardError
        assert result.standardOutput.contains('Result is: 0')
    }

    private TestFile getBuildFile() {
        tmpDir.testDirectory.file('build.gradle')
    }
}
