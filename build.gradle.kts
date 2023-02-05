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
    }
}
val artifactoryUrl: String by project
print(artifactoryUrl)
ext.apply {
    set("artifactoryGroup", "com.arindom.stategenie")
    set("versionName", scmVersion.version)
    set("versionCode", getVersionCode(scmVersion.version))
//    set("versionName", "0.1.0-beta")
//    set("versionCode", "0.1.0")
    set("artifactoryUrl", artifactoryUrl)
}

fun getVersionCode(version: String): Int {
    val tokens = version.split(".", "-")
    val major = tokens[0].toInt()
    val minor = tokens[1].toInt()
    val patch = tokens[2].toInt()
    return major * 10000 + minor * 100 + patch
}