package com.github.jengelman.gradle.plugins.processes

import org.gradle.api.internal.AbstractMultiCauseException

/**
 * Exception that wraps exceptions from multiple forked processes.
 */
public class MultipleProcessException extends AbstractMultiCauseException {

    public MultipleProcessException(Iterable<? extends Throwable> causes) {
        super("Multiple process exceptions", causes)
    }

    public void replaceCauses(List<? extends Throwable> causes) {
        super.initCauses(causes)
    }
}
