package com.github.jengelman.gradle.plugins.processes

import com.github.jengelman.gradle.plugins.processes.tasks.Fork
import com.github.jengelman.gradle.plugins.processes.util.PluginSpecification
import org.gradle.testkit.runner.BuildResult
import spock.lang.Unroll

class GradleVersionCompatibilitySpec extends PluginSpecification {



    @Unroll
    def 'plugin works with Gradle #gradleVersion'() {
        given:
        File testFile = dir.newFile('touchFile')

        runner = runner.withGradleVersion(gradleVersion)

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
        BuildResult result = runner.withArguments('forkMain').build()

        then:
        assert result.output.contains('Process completed')

        where:
        // This branch is compatible from 4.5 onwards.
        gradleVersion << ['4.5']
    }

}
