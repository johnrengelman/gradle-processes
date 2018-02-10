package com.github.jengelman.gradle.plugins.processes.util

import org.gradle.api.internal.file.*
import org.gradle.api.internal.file.collections.DefaultDirectoryFileTreeFactory
import org.gradle.api.internal.file.collections.DirectoryFileTreeFactory
import org.gradle.api.tasks.util.internal.PatternSets
import org.gradle.internal.hash.DefaultContentHasherFactory
import org.gradle.internal.hash.DefaultFileHasher
import org.gradle.internal.hash.DefaultStreamHasher
import org.gradle.internal.nativeintegration.services.NativeServices
import org.gradle.internal.nativeplatform.filesystem.FileSystem
import org.gradle.internal.reflect.DirectInstantiator
import org.gradle.process.internal.DefaultExecActionFactory
import org.gradle.process.internal.ExecFactory

class TestFiles {
    private static final FileSystem FILE_SYSTEM = {
        NativeServices.initialize(new File(System.getProperty('user.dir')))
        NativeServices.instance.get(FileSystem)
    }()

    private static final DefaultFileLookup FILE_LOOKUP = new DefaultFileLookup(FILE_SYSTEM, PatternSets.getNonCachingPatternSetFactory())
    private static final DefaultExecActionFactory EXEC_FACTORY = new DefaultExecActionFactory(resolver())

    static FileLookup fileLookup() {
        return FILE_LOOKUP
    }

    static FileSystem fileSystem() {
        return FILE_SYSTEM
    }

    static FileResolver resolver() {
        return FILE_LOOKUP.getFileResolver()
    }

    static FileResolver resolver(File baseDir) {
        return FILE_LOOKUP.getFileResolver(baseDir)
    }

    static DirectoryFileTreeFactory directoryFileTreeFactory() {
        return new DefaultDirectoryFileTreeFactory(resolver().getPatternSetFactory(), fileSystem())
    }

    static DefaultFileOperations fileOperations(File basedDir) {
        return new DefaultFileOperations(resolver(basedDir), null, null, DirectInstantiator.INSTANCE, fileLookup(), directoryFileTreeFactory(), streamHasher(), fileHasher(), execFactory())
    }

    static DefaultStreamHasher streamHasher() {
        return new DefaultStreamHasher(new DefaultContentHasherFactory())
    }

    static DefaultFileHasher fileHasher() {
        return new DefaultFileHasher(streamHasher())
    }

    static ExecFactory execFactory() {
        return EXEC_FACTORY
    }
}
