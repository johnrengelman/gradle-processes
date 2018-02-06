package com.github.jengelman.gradle.plugins.processes.util

import org.gradle.api.internal.file.FileResolver
import org.gradle.api.internal.file.IdentityFileResolver
import org.gradle.internal.nativeintegration.services.NativeServices
import org.gradle.internal.nativeplatform.filesystem.FileSystem

class TestFiles {
    public static FileSystem fileSystem() {
        return NativeServices.getInstance().get(FileSystem)
    }

    /**
     * Returns a resolver with no base directory.
     */
    public static FileResolver resolver() {
        return new IdentityFileResolver(fileSystem())
    }

    /**
     * Returns a resolver with the given base directory.
     */
    public static FileResolver resolver(File baseDir) {
        return resolver().withBaseDir(baseDir)
    }
}
