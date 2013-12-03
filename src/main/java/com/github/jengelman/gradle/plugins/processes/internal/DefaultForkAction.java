package com.github.jengelman.gradle.plugins.processes.internal;

import static com.github.jengelman.gradle.plugins.processes.internal.ExecHandleWrapper.wrap;

import com.github.jengelman.gradle.plugins.processes.ProcessHandle;
import org.gradle.api.internal.file.FileResolver;
import org.gradle.process.internal.ExecException;
import org.gradle.process.internal.ExecHandle;
import org.gradle.process.internal.ExecHandleBuilder;

public class DefaultForkAction extends ExecHandleBuilder implements ForkAction {

    public DefaultForkAction(FileResolver fileResolver) {
        super(fileResolver);
    }

    public ProcessHandle fork() throws ExecException {
        ExecHandle execHandle = build();
        return wrap(execHandle.start());
    }
}
