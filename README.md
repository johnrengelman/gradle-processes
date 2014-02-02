gradle-processes
================

Gradle Processes Plugin - Create and manage forked processes.

This Gradle plugin provides the capability to create forked processes from a Gradle build. It's primary goal is to
bolster the built in capabilities of Gradle which allow for processing forking only in a synchronous manner.

Compatibility Notes
-------------------

Please note that this plugin relies on some internal Gradle core classes and APIs. As such, it has the potential
for breaking compatibility between various Gradle versions. The table below will try to track those issues.

**Key**
* O => untested
* + => compatible
* - => non-compatbile

<table>
    <thead>
        <tr>
            <th>Plugin Version</th>
            <th>Gradle pre-1.9</th>
            <th>Gradle 1.9</th>
            <th>Gradle 1.10-rc-1+</th>
        </tr>
    </thead>
    <tbody>
        <tr>
            <td>0.1</td>
            <td>O</td>
            <td>+</td>
            <td>-</td>
        </tr>
        <tr>
            <td>0.2</td>
            <td>0</td>
            <td>+</td>
            <td>+</td>
        </tr>
    </tbody>
</table>

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

