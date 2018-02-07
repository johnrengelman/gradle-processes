gradle-processes
================

Gradle Processes Plugin - Create and manage forked processes.

This Gradle plugin provides the capability to create forked processes from a Gradle build. It's primary goal is to
bolster the built in capabilities of Gradle which allow for processing forking only in a synchronous manner.

Compatibility Notes
-------------------

This updated version of the plugin is tested against Gradle versions _4.5 onwards (final versions only)_.

Due to internal gradle API changes (some of which this plugin uses) you have to use the older version 0.3.0 of
the plugin on gradle version < 4.5!

How To Use
----------

*Starting from gradle 4.5*: Apply the plugin to your Gradle build:

```groovy
plugins {
    id 'com.github.johnrengelman.processes' // TODO add version
}
```

*If using a version prior to gradle 4.5*: Apply the plugin to your Gradle build:
```groovy
plugins {
    id "com.github.johnrengelman.processes" version "0.3.0"
}
```


Capabilities
------------

### Process Forking

```
ProcessHandle handle = project.procs.fork {
    <process configuration> See [Exec](http://www.gradle.org/docs/current/dsl/org.gradle.api.tasks.Exec.html)
}

ProcessHandle handle = project.procs.javafork {
    <process configuration> See [JavaExec](http://www.gradle.org/docs/current/dsl/org.gradle.api.tasks.JavaExec.html)
}
```

### Process Joining

```groovy
project.procs.waitForFinish(handle)
```

### Handling Multiple Processes

```groovy
ProcessHandle handle = project.procs.javafork { ... }
ProcessHandle handle2 = project.procs.javafork { ... }
project.procs.waitForFinish([handle, handle2])
```

### Forking Tasks

```groovy
task fork(type: Fork) {
  // Configure the same as the Exec task
}

task javafork(type: JavaFork) {
  // Configure the same as the JavaExec task
}
```

### Accessing a Forked Tasks Process

```groovy
task fork(type: Fork) {
  // Configure task
}

task waitForFork() << {
  fork.processHandle.waitForFinish()
}
```

Using the forked tasks can allow for starting/stopping processes for things like test infrastructure.

```groovy
task startServer(type: Fork) {
  // Start some external service
}

task stopServer << {
  startServer.processHandle.abort()
}

test.dependsOn startServer
test.finalizedBy stopServer
```

# Building the plugin

This should build everything and run the tests:

```bash
./gradlew build
```