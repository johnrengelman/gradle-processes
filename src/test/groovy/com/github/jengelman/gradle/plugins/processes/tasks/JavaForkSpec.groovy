package com.github.jengelman.gradle.plugins.processes.tasks

import com.github.jengelman.gradle.plugins.processes.ProcessHandle
import com.github.jengelman.gradle.plugins.processes.ProcessHandleListener
import com.github.jengelman.gradle.plugins.processes.ProcessesPlugin
import com.github.jengelman.gradle.plugins.processes.util.PluginSpecification
import com.github.jengelman.gradle.plugins.processes.util.TestMain
import org.gradle.internal.classloader.ClasspathUtil
import org.gradle.process.ExecResult
import org.gradle.testkit.runner.BuildResult

class JavaForkSpec extends PluginSpecification {

    def setup() {
        buildFile << """
        apply plugin: ${ProcessesPlugin.name}
        """
    }

    @SuppressWarnings(['Println', 'ClosureAsLastMethodParameter'])
    def forkTask() {
        given:
        File testFile = file('someFile')
        List<URL> files = ClasspathUtil.getClasspath(this.class.classLoader)
        buildFile << """
        List cp = ${files.collect { 'new URL("' + it.toString() + '")' } }
        task javaForkMain(type: ${JavaFork.name}) {
            classpath(cp as Object[])
            main = '${TestMain.name}'
            args '${testFile.absolutePath}'
            listener(new ${ProcessHandleListener.name}() {
                void executionStarted(${ProcessHandle.name} handle) {
                    println 'Execution Started'
                }
                void executionFinished(${ProcessHandle.name} handle, ${ExecResult.name} result) {
                    println 'Execution Finished'
                }
            })
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
        runner.arguments << 'javaForkMain'
        BuildResult result = runner.run()

        then:
        assert result.standardOutput.contains('Process completed')
        assert result.standardOutput.contains('Execution Started')
        assert result.standardOutput.contains('Execution Finished')
    }
}
