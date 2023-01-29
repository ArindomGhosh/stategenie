package com.arindom.stategenie.annotations

@Retention(
    AnnotationRetention.SOURCE
)
@Target(
    AnnotationTarget.CLASS
)
annotation class GenieState(
    val rootName: String = "",
    val isParcelable: Boolean = false
) {
    companion object {
        const val ROOT_NAME = "rootName"
        const val IS_PARCELABLE = "isParcelable"
    }
}

@Retention(
    AnnotationRetention.SOURCE
)
@Target(
    AnnotationTarget.PROPERTY
)
annotation class ToState(val stateName: String = ""){
    companion object{
        const val STATE_NAME = "stateName"
    }
}