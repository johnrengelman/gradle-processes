package com.github.jengelman.gradle.plugins.processes.internal;

import static com.github.jengelman.gradle.plugins.processes.internal.ExecHandleWrapper.wrap;

import com.github.jengelman.gradle.plugins.processes.ProcessHandle;
import com.github.jengelman.gradle.plugins.processes.ProcessHandleListener;
import org.gradle.api.internal.file.FileResolver;
import org.gradle.initialization.DefaultBuildCancellationToken;
import org.gradle.process.internal.ExecHandle;
import org.gradle.process.internal.JavaExecHandleBuilder;

import java.util.concurrent.Executor;

public class DefaultJavaForkAction extends JavaExecHandleBuilder implements JavaForkAction {

    public DefaultJavaForkAction(FileResolver fileResolver, Executor executor) {
        super(fileResolver, executor, new DefaultBuildCancellationToken());
    }

    public ProcessHandle fork() {
        ExecHandle execHandle = build();
        return wrap(execHandle.start(), isIgnoreExitValue());
    }

    @Override
    public JavaForkAction listener(ProcessHandleListener listener) {
        super.listener(new ExecHandleListenerAdapter(listener));
        return this;
    }
}
