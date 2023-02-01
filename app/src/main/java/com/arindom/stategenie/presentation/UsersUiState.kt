package com.arindom.stategenie.presentation

import com.arindom.stategenie.annotations.GenieState

@GenieState(
    rootName = "UsersUiState"
)
interface UsersUiState: BaseUIiState<List<User>>

data class UserList(
    val users: List<User> = emptyList()
)

data class User(
    val name: String,
    val email: String
)