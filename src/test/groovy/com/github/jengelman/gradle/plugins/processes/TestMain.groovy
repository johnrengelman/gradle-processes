package com.github.jengelman.gradle.plugins.processes

import org.apache.commons.io.FileUtils

class TestMain {

    static void main(String[] args) {
        FileUtils.touch(new File(args[0]))
    }
}
