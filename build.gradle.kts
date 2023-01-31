buildscript {

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

ext["artifactoryGroup"] = "com.arindom.stategenie"
ext["versionName"] = scmVersion.version
ext["versionCode"] = getVersionCode(scmVersion.version)

fun getVersionCode(version: String): Int {
    val tokens = version.split(".", "-")
    val major = tokens[0].toInt()
    val minor = tokens[1].toInt()
    val patch = tokens[2].toInt()
    return major * 10000 + minor * 100 + patch
}