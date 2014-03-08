package com.github.jengelman.gradle.plugins.processes.util

import org.gradle.testkit.functional.GradleRunner
import org.gradle.testkit.functional.GradleRunnerFactory
import org.gradle.testkit.functional.internal.DefaultGradleRunner
import org.gradle.testkit.functional.internal.GradleHandle
import org.gradle.testkit.functional.internal.GradleHandleFactory
import org.gradle.testkit.functional.internal.classpath.ClasspathInjectingGradleHandleFactory
import org.gradle.testkit.functional.internal.toolingapi.BuildLauncherBackedGradleHandle
import org.gradle.testkit.functional.internal.toolingapi.ToolingApiGradleHandleFactory
import org.gradle.tooling.BuildLauncher
import org.gradle.tooling.GradleConnector
import org.gradle.tooling.ProjectConnection

public class GradleVersionRunnerFactory {

    public static GradleRunner create(Closure connectorConfiguration) {
        GradleHandleFactory toolingApiHandleFactory = new ConfigurableToolingApiHandleFactory(connectorConfiguration);

        // TODO: Which class would be attached to the right classloader? Is using something from the test kit right?
        ClassLoader sourceClassLoader = GradleRunnerFactory.class.getClassLoader();
        GradleHandleFactory classpathInjectingHandleFactory = new ClasspathInjectingGradleHandleFactory(sourceClassLoader, toolingApiHandleFactory);

        return new DefaultGradleRunner(classpathInjectingHandleFactory);
    }

}

class ConfigurableToolingApiHandleFactory extends ToolingApiGradleHandleFactory {

    Closure connectorConfiguration

    ConfigurableToolingApiHandleFactory(Closure connectorConfiguration) {
        this.connectorConfiguration = connectorConfiguration
    }

    @Override
    public GradleHandle start(File directory, List<String> arguments) {
        GradleConnector connector = GradleConnector.newConnector();
        connector.forProjectDirectory(directory);
        connectorConfiguration.delegate = connector
        connectorConfiguration(connector)
        ProjectConnection connection = connector.connect();
        BuildLauncher launcher = connection.newBuild();
        String[] argumentArray = new String[arguments.size()];
        arguments.toArray(argumentArray);
        launcher.withArguments(argumentArray);
        return new BuildLauncherBackedGradleHandle(launcher);
    }
}
