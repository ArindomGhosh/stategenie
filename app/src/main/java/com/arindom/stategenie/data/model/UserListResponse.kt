package com.arindom.stategenie.data.model

data class UserListResponse(
    val data: List<UserResponse>
)

data class UserResponse(
    val login: String,
    val avatar_url: String
)
