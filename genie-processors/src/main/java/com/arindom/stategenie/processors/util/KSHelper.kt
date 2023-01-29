package com.arindom.stategenie.processors.util

import com.google.devtools.ksp.symbol.KSAnnotation

fun Sequence<KSAnnotation>.getAnnotation(target: String): KSAnnotation {
    return getAnnotationIfExist(target)
        ?: throw NoSuchElementException("Sequence contains no element matching the $target.")
}

fun Sequence<KSAnnotation>.getAnnotationIfExist(target: String): KSAnnotation? {
    for (element in this) if (element.shortName.asString() == target) return element
    return null
}

fun Sequence<KSAnnotation>.hasAnnotation(target: String): Boolean {
    for (element in this) if (element.shortName.asString() == target) return true
    return false
}

