package com.github.jengelman.gradle.plugins.processes.internal

import com.github.jengelman.gradle.plugins.processes.MultipleProcessException
import com.github.jengelman.gradle.plugins.processes.ProcessOperations
import com.github.jengelman.gradle.plugins.processes.ProcessHandle
import org.gradle.api.Action
import org.gradle.api.internal.file.FileOperations
import org.gradle.api.internal.file.FileResolver
import org.gradle.api.internal.ProcessOperations as GradleProcessOperations
import org.gradle.internal.concurrent.DefaultExecutorFactory
import org.gradle.internal.concurrent.ExecutorFactory
import org.gradle.internal.reflect.Instantiator
import org.gradle.process.ExecResult
import org.gradle.process.ExecSpec
import org.gradle.process.JavaExecSpec
import org.gradle.process.internal.ExecException
import org.gradle.util.ConfigureUtil

import java.util.concurrent.Executor

/**
 * Implementation of process interactions
 */
class DefaultProcessOperations implements ProcessOperations {

    private final Instantiator instantiator
    private final FileResolver fileResolver
    private final GradleProcessOperations processOperations
    private final ExecutorFactory executorFactory = new DefaultExecutorFactory()

    DefaultProcessOperations(Instantiator instantiator, FileResolver fileResolver,
                             GradleProcessOperations processOperations) {
        assert instantiator
        assert fileResolver
        assert processOperations
        this.instantiator = instantiator
        this.fileResolver = fileResolver
        this.processOperations = processOperations
    }

    @Override
    ProcessHandle javafork(Closure cl) {
        JavaForkAction javaForkAction =
                ConfigureUtil.configure(cl, instantiator.newInstance(DefaultJavaForkAction, fileResolver, executor()))
        return javaForkAction.fork()
    }

    @Override
    ProcessHandle fork(Closure cl) {
        ForkAction forkAction =
                ConfigureUtil.configure(cl, instantiator.newInstance(DefaultForkAction, fileResolver, executor()))
        return forkAction.fork()
    }

    @Override
    ExecResult waitForFinish(ProcessHandle fork) {
        ExecResult result = fork.waitForFinish()
        if (!fork.isIgnoreExitValue()) {
            result.assertNormalExitValue()
        }
        return result
    }

    @Override
    List<ExecResult> waitForFinish(List<ProcessHandle> forks) {
        List<ExecResult> results = []
        List<Throwable> throwables = []
        forks.each { fork ->
            try {
                ExecResult result = waitForFinish(fork)
                results.add(result)
            } catch (ExecException e) {
                throwables.add(e)
            }
        }
        if (!throwables.isEmpty()) {
            throw new MultipleProcessException(throwables)
        }
        return results
    }

    private Executor executor() {
        executorFactory.create("gradle-processes")
    }



    @Override
    ForkAction newForkAction() {
        return new DefaultForkAction(fileResolver, executor())
    }

    @Override
    JavaForkAction newJavaForkAction() {
        return new DefaultJavaForkAction(fileResolver, executor())
    }

    @Override
    ExecResult javaexec(Action<? super JavaExecSpec> action) {
        return processOperations.javaexec(action)
    }

    @Override
    ExecResult exec(Action<? super ExecSpec> action) {
        return processOperations.exec(action)
    }
}
