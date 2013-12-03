package com.github.jengelman.gradle.plugins.processes.internal;

import com.github.jengelman.gradle.plugins.processes.ProcessHandle;
import org.gradle.api.NonExtensible;
import org.gradle.process.ExecSpec;
import org.gradle.process.internal.ExecException;

@NonExtensible
public interface ForkAction extends ExecSpec {

    ProcessHandle fork() throws ExecException;

}
