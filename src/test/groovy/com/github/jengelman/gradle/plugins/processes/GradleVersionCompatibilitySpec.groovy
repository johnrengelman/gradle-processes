package com.github.jengelman.gradle.plugins.processes

import com.github.jengelman.gradle.plugins.processes.tasks.Fork
import com.github.jengelman.gradle.plugins.processes.util.GradleVersionRunnerFactory
import com.github.jengelman.gradle.plugins.processes.util.PluginSpecification
import org.gradle.testkit.functional.ExecutionResult
import org.gradle.tooling.GradleConnector
import spock.lang.Unroll

class GradleVersionCompatibilitySpec extends PluginSpecification {

    @Unroll
    def 'plugin works with Gradle #version'() {
        given:
        File testFile = dir.newFile('touchFile')
        runner = GradleVersionRunnerFactory.create { GradleConnector connector ->
            connector.useGradleVersion(gradleVersion)
        }
        runner.directory = dir.root
        buildFile << """
        apply plugin: ${ProcessesPlugin.name}

        task forkMain(type: ${Fork.name}) {
            executable = 'touch'
            workingDir = "${dir.root}"
            args "${testFile.path}"
        }

        task waitForFinish() {
            doLast {
                forkMain.processHandle.waitForFinish().assertNormalExitValue()
                println 'Process completed'
            }
        }

        forkMain.finalizedBy waitForFinish
        """

        when:
        runner.arguments << 'forkMain'
        ExecutionResult result = runner.run()

        then:
        assert result.standardOutput.contains('Process completed')

        where:
        gradleVersion << ['1.8', '1.9', '1.10', '1.11']
    }

}
