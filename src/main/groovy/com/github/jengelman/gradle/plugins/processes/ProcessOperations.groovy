package com.github.jengelman.gradle.plugins.processes

import com.github.jengelman.gradle.plugins.processes.internal.BlockingProcessOperations
import com.github.jengelman.gradle.plugins.processes.internal.ForkActionFactory
import com.github.jengelman.gradle.plugins.processes.internal.NonBlockingProcessOperations

/**
 * Definition of all process handling interfaces
 */
public interface ProcessOperations extends NonBlockingProcessOperations, BlockingProcessOperations, ForkActionFactory {

}