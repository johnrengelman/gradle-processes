package com.github.jengelman.gradle.plugins.processes.internal;

import com.github.jengelman.gradle.plugins.processes.ProcessHandle;
import org.gradle.api.NonExtensible;
import org.gradle.process.JavaExecSpec;

@NonExtensible
public interface JavaForkAction extends JavaExecSpec {

    ProcessHandle fork();

}
