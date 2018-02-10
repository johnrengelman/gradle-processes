package com.github.jengelman.gradle.plugins.processes.util

import com.github.jengelman.gradle.plugins.processes.ProcessesPlugin
import org.gradle.testkit.runner.GradleRunner
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification

class PluginSpecification extends Specification {

    @Rule
    TemporaryFolder dir
    GradleRunner runner

    def setup() {
        runner = GradleRunner.create()
                .withProjectDir(dir.root)
                .withPluginClasspath()

        buildFile << """
        plugins {
            id '${ProcessesPlugin.IDENTIFIER}'
        }
        """
    }

    File getBuildFile() {
        file('build.gradle')
    }

    File getSettingsFile() {
        file('settings.gradle')
    }

    File file(String path) {
        File f = new File(dir.root, path)
        if (!f.exists()) {
            f.parentFile.mkdirs()
            return dir.newFile(path)
        }
        return f
    }
}
