gradle-processes
================

Gradle Processes Plugin - Create and manage forked processes.

This Gradle plugin provides the capability to create forked processes from a Gradle build. It's primary goal is to
bolster the built in capabilities of Gradle which allow for processing forking only in a synchronous manner.

Compatibility Notes
-------------------

Version 0.1 of this plugin in compatible only with versions of Gradle before v1.10-rc-1. This was due to a reliance on
an internal Gradle class that was moved between v1.9 and 1.10. This issue has been corrected in v0.2 of the plugin
and compatability should no longer be an issue.

How To Use
----------

First, add the BinTray JCenter repository and the plugin library to your build's buildscript.

    buildscript {
        respositories {
           jcenter()
        }
        dependencies {
           classpath 'com.github.jengelman.gradle.plugins:gradle-processes:0.1'
        }
    }

Second, apply the plugin to your Gradle build:

    apply plugin: 'processes'

Capabilities
------------

### Process Forking

    ProcessHandle handle = project.procs.fork {
        <process configuration> See [Exec](http://www.gradle.org/docs/current/dsl/org.gradle.api.tasks.Exec.html)
    }

    ProcessHandle handle = project.procs.javafork {
        <process configuration> See [JavaExec](http://www.gradle.org/docs/current/dsl/org.gradle.api.tasks.JavaExec.html)
    }

### Process Joining

    project.procs.waitForFinish(handle)

### Handling Multiple Processes

    ProcessHandle handle = project.procs.javafork { ... }
    ProcessHandle handle2 = project.procs.javafork { ... }
    project.procs.waitForFinish([handle, handle2])

