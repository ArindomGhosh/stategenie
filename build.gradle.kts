buildscript {
    repositories {
        mavenCentral()
    }

    dependencies {
        classpath("org.jfrog.buildinfo:build-info-extractor-gradle:4+")
    }
}

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.axion)
}

scmVersion {
    checks {
        aheadOfRemote.set(false)
    }

    repository {
        pushTagsOnly.set(true)
        remote.set("pushback")
    }
}

ext.apply {
    set("artifactoryGroup", "com.arindom.stategenie")
    set("versionName", scmVersion.version)
    set("versionCode", getVersionCode(scmVersion.version))
    if (System.getenv("ARTIFACTORY_URL") != null)
        set("artifactoryUrl", System.getenv("ARTIFACTORY_URL"))
    if (System.getenv("USER_NAME") != null)
        set("artifactoryUserName", System.getenv("USER_NAME"))
    if (System.getenv("PASSWORD") != null)
        set("artifactoryPassword", System.getenv("PASSWORD"))
    if (System.getenv("REPOSITORY_KEY") != null)
        set("repositoryKey", System.getenv("REPOSITORY_KEY"))
}

fun getVersionCode(version: String): Int {
    val tokens = version.split(".", "-")
    val major = tokens[0].toInt()
    val minor = tokens[1].toInt()
    val patch = tokens[2].toInt()
    return major * 10000 + minor * 100 + patch
}