package com.github.jengelman.gradle.plugins.processes

import com.github.jengelman.gradle.plugins.processes.tasks.Fork
import com.github.jengelman.gradle.plugins.processes.util.PluginSpecification
import org.gradle.api.GradleException
import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.TaskOutcome
import org.gradle.testkit.runner.UnexpectedBuildFailure
import spock.lang.Unroll

class GradleVersionCompatibilitySpec extends PluginSpecification {

    @Unroll
    def 'plugin works with Gradle #gradleVersion'() {
        given:
        def touchedFile = dir.newFile('touchedFile')
        runner = runner.withGradleVersion(gradleVersion)

        buildFile << """
        task touchAFile(type: ${Fork.name}) {
            executable = 'touch'
            workingDir = "${dir.root}"
            args "${touchedFile.path}"
        }

        task waitForFinish() {
            doLast {
                touchAFile.processHandle.waitForFinish().assertNormalExitValue()
                println 'Process completed'
            }
        }

        touchAFile.finalizedBy waitForFinish
        """

        when:
        BuildResult result = runner.withArguments('touchAFile').build()

        then:
        assert result.output.contains('Process completed')

        where:
        gradleVersion << ['4.6', '4.7', '4.8', '4.8.1', '4.9']
    }

    @Unroll
    def 'plugin should fail with a nice exception when using gradle #gradleVersion'() {
        given:
        runner = runner.withGradleVersion(gradleVersion)

        when:
        runner.withArguments('tasks').build()

        then:
        def exception = thrown(UnexpectedBuildFailure)

        assert exception.message.contains('This version of the plugin is incompatible with gradle < 4.6!')

        where:
        gradleVersion << ['4.5.1', '4.4.1', '3.5']
    }

}
