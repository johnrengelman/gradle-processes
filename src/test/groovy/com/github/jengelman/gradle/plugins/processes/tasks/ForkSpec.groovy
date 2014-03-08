package com.github.jengelman.gradle.plugins.processes.tasks

import com.github.jengelman.gradle.plugins.processes.ProcessesPlugin
import com.github.jengelman.gradle.plugins.processes.util.PluginSpecification
import org.gradle.testkit.functional.ExecutionResult

class ForkSpec extends PluginSpecification {

    def setup() {
        buildFile << """
        apply plugin: ${ProcessesPlugin.name}
        """
    }

    @SuppressWarnings('Println')
    def forkTask() {
        given:
        File testFile = file('someFile')

        buildFile << """
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
    }
}
