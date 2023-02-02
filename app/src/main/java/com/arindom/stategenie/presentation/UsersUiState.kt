package com.arindom.stategenie.presentation

import com.arindom.stategenie.annotations.StateGenie
import com.arindom.stategenie.data.model.UserResponse

@StateGenie(
    rootName = "UsersUiState"
)
interface UsersUiState: BaseUIiState<List<UserResponse>>

data class UserList(
    val users: List<User> = emptyList()
)

data class User(
    val name: String,
    val email: String
)