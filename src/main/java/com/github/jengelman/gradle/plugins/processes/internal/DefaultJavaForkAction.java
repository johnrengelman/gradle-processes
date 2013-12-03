package com.github.jengelman.gradle.plugins.processes.internal;

import static com.github.jengelman.gradle.plugins.processes.internal.ExecHandleWrapper.wrap;

import com.github.jengelman.gradle.plugins.processes.ProcessHandle;
import org.gradle.api.internal.file.FileResolver;
import org.gradle.process.internal.ExecHandle;
import org.gradle.process.internal.JavaExecHandleBuilder;

public class DefaultJavaForkAction extends JavaExecHandleBuilder implements JavaForkAction {

    public DefaultJavaForkAction(FileResolver fileResolver) {
        super(fileResolver);
    }

    public ProcessHandle fork() {
        ExecHandle execHandle = build();
        return wrap(execHandle.start());
    }
}
