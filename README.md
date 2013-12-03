gradle-processes
================

Gradle Processes Plugin - Create and manage forked processes.

This Gradle plugin provides the capability to create forked processes from a Gradle build. It's primary goal is to
bolster the built in capabilities of Gradle which allow for processing forking only in a synchronous manner.

How To Use
----------

First, add the BinTray JCenter repository and the plugin library to your build's buildscript.

    buildscript {
        respositories {
           jcenter()
        }
        dependencies {
           classpath 'com.github.jengelman.gradle.plugins:processes:0.1'
        }
    }

Second, apply the plugin to your Gradle build:

    apply plugin: 'processes'

Capabilities
------------

### Process Forking

### Process Joining

