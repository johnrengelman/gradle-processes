package com.github.jengelman.gradle.plugins.processes.util

import org.gradle.testkit.runner.GradleRunner

public class GradleVersionRunnerFactory {

    public static GradleRunner create(Closure connectorConfiguration) {
        GradleRunner.create()
    }
}
